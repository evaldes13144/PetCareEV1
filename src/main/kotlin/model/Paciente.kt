package model

import java.time.LocalDateTime

open class Paciente(
    val codigoAtencion: String,
    val nombre: String,
    val especie: String,
    val fechaIngreso: LocalDateTime = LocalDateTime.now(),
    val tipoDuenio: TipoDuenio,
    val tarifaBaseHora: Double
) {
    init {
        val patron = Regex("^[A-Za-z]{2}\\d{2}[A-Za-z]{2}$")
        if (!patron.matches(codigoAtencion)) {
            throw IllegalArgumentException("Codigo de atencion '$codigoAtencion' invalido.")
        }
    }

    open fun calcularCostoBase(minutosUso: Int): Double {
        val horas = minutosUso / 60.0
        return horas * tarifaBaseHora
    }

    open fun obtenerTipo(): String = "General"
}

class Canino(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    tipoDuenio: TipoDuenio
) : Paciente(codigoAtencion, nombre, especie, LocalDateTime.now(), tipoDuenio, 12000.0) {

    override fun calcularCostoBase(minutosUso: Int): Double {
        val base = super.calcularCostoBase(minutosUso)

        return if (tipoDuenio is DuenioConvenio) {
            base * 0.80
        } else {
            base
        }
    }

    override fun obtenerTipo(): String = "Canino"
}

class Felino(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    tipoDuenio: TipoDuenio
) : Paciente(codigoAtencion, nombre, especie, LocalDateTime.now(), tipoDuenio, 9000.0) {

    override fun calcularCostoBase(minutosUso: Int): Double {
        if (minutosUso < 20) {
            return 0.0
        }
        return super.calcularCostoBase(minutosUso)
    }

    override fun obtenerTipo(): String = "Felino"
}

class Exotico(
    codigoAtencion: String,
    nombre: String,
    especie: String,
    tipoDuenio: TipoDuenio,
    val esSilvestre: Boolean
) : Paciente(codigoAtencion, nombre, especie, LocalDateTime.now(), tipoDuenio, 20000.0) {

    override fun calcularCostoBase(minutosUso: Int): Double {
        val base = super.calcularCostoBase(minutosUso)
        return if (esSilvestre) {
            base * 1.30
        } else {
            base
        }
    }

    override fun obtenerTipo(): String = "Exotico (Silvestre: ${if (esSilvestre) "Si" else "No"})"
}