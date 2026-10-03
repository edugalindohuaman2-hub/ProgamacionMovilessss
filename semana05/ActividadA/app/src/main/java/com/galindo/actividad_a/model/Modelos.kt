package com.galindo.actividad_a.model

// Representa a un médico de la clínica
data class Medico(
    val id: String,
    val nombre: String,
    val especialidad: String,
    val calificacion: Float
)

// Representa una cita ya agendada por el usuario
data class Cita(
    val medico: Medico,
    val fecha: String,
    val hora: String,
    var estado: String = "Confirmada" // puede ser "Confirmada" o "Completada"
)