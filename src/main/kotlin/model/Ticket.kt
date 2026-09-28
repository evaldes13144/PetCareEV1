package model

data class Ticket(
    val numeroTicket: Int,
    val paciente: Paciente,
    val minutosUso: Int,
    val montoFinal: Double
)