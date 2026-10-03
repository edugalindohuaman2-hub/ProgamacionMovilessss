package com.tecsupfit.app.model

data class Reserva(
    val clase: Clase,
    val horarioSeleccionado: String,
    val estado: String = "Confirmada"   // "Confirmada" o "Completada"
)
