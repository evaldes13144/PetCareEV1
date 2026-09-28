package model

open class TipoDuenio(val nombre: String) {
    override fun toString(): String = nombre
}

class DuenioParticular : TipoDuenio("Particular")
class DuenioConvenio : TipoDuenio("Convenio")
class DuenioMunicipal : TipoDuenio("Municipal")