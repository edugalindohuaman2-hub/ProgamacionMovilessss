package com.galindo.actividad_a.data

import com.galindo.actividad_a.model.Medico

// Lista de especialidades para los chips de filtro en Inicio
val listaEspecialidades = listOf("Todas", "Cardiología", "Pediatría", "Dermatología")

val listaMedicos = listOf(
    Medico("1", "Dra. Ana Torres", "Cardiología", 4.8f),
    Medico("2", "Dr. Luis Ramos", "Pediatría", 4.5f),
    Medico("3", "Dra. Carla Ruiz", "Dermatología", 4.9f),
    Medico("4", "Dr. Jorge Paredes", "Cardiología", 4.2f)
)

// Opciones de fecha y hora para la pantalla de Agendar cita
val listaFechas = listOf("Lun 29 Set", "Mar 30 Set", "Mié 1 Oct")
val listaHoras = listOf("09:00 am", "11:00 am", "03:00 pm")