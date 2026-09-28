package service

import kotlinx.coroutines.delay
import model.*

class SistemaPetCare(totalBoxes: Int = 10) {
    val boxes: List<Box> = (1..totalBoxes).map { Box(numero = it) }
    val historialTickets: MutableList<Ticket> = mutableListOf()
    private var contadorTickets = 1000

    suspend fun registrarEntrada(paciente: Paciente): Boolean {
        val boxLibre = boxes.firstOrNull { it.estado is EstadoBox.Libre }
        if (boxLibre == null) {
            println("[ERROR] Sistema sin capacidad: No hay boxes libres disponibles.")
            return false
        }

        boxLibre.estado = EstadoBox.EnProceso("Registrando entrada para ${paciente.codigoAtencion}")
        println("[SENSOR] Box #${boxLibre.numero}: Conectando con sensor físico (3.0s)...")
        delay(3000L)

        boxLibre.estado = EstadoBox.EnAtencion(paciente)
        println("[ÉXITO] Paciente '${paciente.nombre}' [${paciente.codigoAtencion}] asignado al Box #${boxLibre.numero}.")
        return true
    }

    suspend fun registrarSalida(codigoAtencion: String, minutosUso: Int): Ticket? {
        val boxOcupado = boxes.firstOrNull { box ->
            when (val estado = box.estado) {
                is EstadoBox.EnAtencion -> estado.paciente.codigoAtencion.equals(codigoAtencion, ignoreCase = true)
                else -> false
            }
        }

        if (boxOcupado == null) {
            println("[ERROR] Paciente no encontrado: No hay atencion activa con codigo '$codigoAtencion'.")
            return null
        }

        val paciente = (boxOcupado.estado as EstadoBox.EnAtencion).paciente

        boxOcupado.estado = EstadoBox.EnProceso("Calculando salida de $codigoAtencion")
        println("[SENSOR] Box #${boxOcupado.numero}: Procesando salida y sensor (6.5s)...")
        delay(6500L)

        val total = try {
            CalculadoraTarifa.calcularTotal(paciente, minutosUso)
        } catch (e: Exception) {
            println("[ERROR] En cálculo de tarifa: ${e.message}")
            boxOcupado.estado = EstadoBox.EnAtencion(paciente)
            return null
        }

        contadorTickets++
        val ticket = Ticket(contadorTickets, paciente, minutosUso, total)
        historialTickets.add(ticket)

        boxOcupado.estado = EstadoBox.Libre
        println("[ÉXITO] Box #${boxOcupado.numero} liberado. Ticket #${ticket.numeroTicket} emitido a ${paciente.nombre}.")
        return ticket
    }

    // --- Consultas de Negocio usando Programación Funcional ---
    fun contarBoxesDisponibles(): Int =
        boxes.count { it.estado is EstadoBox.Libre }

    fun pacientesConvenioHistorial(): List<Paciente> =
        historialTickets.map { it.paciente }.filter { it.tipoDuenio is DuenioConvenio }

    fun calcularIngresoPromedio(): Double =
        if (historialTickets.isEmpty()) 0.0 else historialTickets.map { it.montoFinal }.average()

    fun codigosFinalizados(): List<String> =
        historialTickets.map { it.paciente.codigoAtencion }

    fun pacienteMayorTiempo(): Ticket? =
        historialTickets.maxByOrNull { it.minutosUso }

    fun totalRecaudado(): Double =
        historialTickets.sumOf { it.montoFinal }

    fun tipoConMayorIngreso(): String {
        if (historialTickets.isEmpty()) return "Sin datos"
        return historialTickets.groupBy { it.paciente.javaClass.simpleName }
            .mapValues { entry -> entry.value.sumOf { it.montoFinal } }
            .maxByOrNull { it.value }?.key ?: "N/A"
    }

    fun imprimirReporteCierre() {
        println("\n==========================================================================")
        println("                    REPORTE DE CIERRE DE TURNO - PETCARE                  ")
        println("==========================================================================")
        if (historialTickets.isEmpty()) {
            println("No se registraron atenciones durante este turno.")
        } else {
            println("%-8s | %-10s | %-8s | %-10s | %-12s".format("TICKET", "TIPO", "CÓDIGO", "TIEMPO", "MONTO"))
            println("--------------------------------------------------------------------------")
            historialTickets.forEach { t ->
                println("%-8d | %-10s | %-8s | %-4d min  | $%,10.0f".format(
                    t.numeroTicket,
                    t.paciente.javaClass.simpleName,
                    t.paciente.codigoAtencion,
                    t.minutosUso,
                    t.montoFinal
                ))
            }
        }
        println("--------------------------------------------------------------------------")
        println("Total Recaudado:                 $%,12.0f".format(totalRecaudado()))
        println("Pacientes Atendidos:             ${historialTickets.size}")
        println("Ingreso Promedio por Atención:   $%,12.0f".format(calcularIngresoPromedio()))
        println("Tipo de Paciente con Más Ingreso: ${tipoConMayorIngreso()}")
        println("Boxes Disponibles al Cierre:     ${contarBoxesDisponibles()} de ${boxes.size}")
        println("==========================================================================\n")
    }
}