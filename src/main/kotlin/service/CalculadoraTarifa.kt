package service

import model.Paciente
import model.DuenioMunicipal

object CalculadoraTarifa {
    private const val IVA = 0.19

    fun calcularTotal(paciente: Paciente, minutosUso: Int): Double {
        if (minutosUso <= 0) {
            throw IllegalArgumentException("El tiempo de uso debe ser mayor a 0 minutos.")
        }

        val costoBase = paciente.calcularCostoBase(minutosUso)
        if (costoBase == 0.0) {
            return 0.0
        }

        val conIva = costoBase * (1.0 + IVA)

        val totalFinal: Double = if (paciente.tipoDuenio is DuenioMunicipal) {
            conIva * 0.50
        } else {
            conIva
        }

        if (totalFinal < 0.0) {
            throw IllegalStateException("Monto invalido: no puede ser negativo ($totalFinal).")
        }

        return totalFinal
    }
}