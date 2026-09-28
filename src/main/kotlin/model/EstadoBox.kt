package model

sealed class EstadoBox {
    object Libre : EstadoBox() {
        override fun toString(): String = "Libre"
    }

    data class EnAtencion(val paciente: Paciente) : EstadoBox() {
        override fun toString(): String = "En Atencion (${paciente.nombre} - ${paciente.codigoAtencion})"
    }

    data class EnProceso(val motivo: String) : EstadoBox() {
        override fun toString(): String = "En Proceso ($motivo)"
    }

    data class FueraDeServicio(val motivo: String) : EstadoBox() {
        override fun toString(): String = "Fuera de Servicio ($motivo)"
    }
}