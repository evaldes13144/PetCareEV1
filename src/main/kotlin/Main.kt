import kotlinx.coroutines.runBlocking
import model.*
import service.SistemaPetCare

fun main() = runBlocking {
    println("==========================================================")
    println("           INICIO SISTEMA DE ATENCION PETCARE             ")
    println("==========================================================\n")

    val sistema = SistemaPetCare(totalBoxes = 10)

    println("Prueba de codigo invalido '123ABC':")
    try {
        val pacienteErroneo = Canino("123ABC", "Toby", "Poodle", DuenioParticular())
        sistema.registrarEntrada(pacienteErroneo)
    } catch (e: IllegalArgumentException) {
        println("[CONTROLADO] ${e.message}\n")
    }

    val p1 = Canino("CA12CD", "Max", "Golden Retriever", DuenioConvenio())
    val p2 = Canino("CA99ZA", "Luna", "Labrador", DuenioParticular())
    val p3 = Felino("FE22TO", "Misi", "Siames", DuenioParticular())
    val p4 = Exotico("EX44RG", "Loro", "Amazonico", DuenioMunicipal(), esSilvestre = true)
    val p5 = Exotico("EX77RG", "Iguana", "Verde", DuenioParticular(), esSilvestre = false)

    println("--> Registrando entradas de pacientes:")
    sistema.registrarEntrada(p1)
    sistema.registrarEntrada(p2)
    sistema.registrarEntrada(p3)
    sistema.registrarEntrada(p4)
    sistema.registrarEntrada(p5)

    println("\nBoxes libres actuales: ${sistema.contarBoxesDisponibles()}\n")

    println("--> Registrando salidas y calculando cobros:")
    sistema.registrarSalida("CA12CD", 75)
    sistema.registrarSalida("CA99ZA", 180)
    sistema.registrarSalida("FE22TO", 18)
    sistema.registrarSalida("EX44RG", 120)
    sistema.registrarSalida("EX77RG", 45)

    println("\n--> [TEST] Prueba de salida con paciente inexistente 'ZZ99XX':")
    sistema.registrarSalida("ZZ99XX", 60)

    println("\n==========================================================")
    println("                   CONSULTAS DE NEGOCIO                   ")
    println("==========================================================")
    println("1. Boxes disponibles ahora: ${sistema.contarBoxesDisponibles()}")
    println("2. Pacientes con convenio atendidos: ${sistema.pacientesConvenioHistorial().map { it.nombre }}")
    println("3. Ingreso promedio por atencion: $%,.0f".format(sistema.calcularIngresoPromedio()))
    println("4. Códigos de atenciones cerradas: ${sistema.codigosFinalizados()}")
    val pacienteMax = sistema.pacienteMayorTiempo()
    println("5. Paciente con mayor tiempo: ${pacienteMax?.paciente?.nombre} (${pacienteMax?.minutosUso} minutos)")

    sistema.imprimirReporteCierre()
}