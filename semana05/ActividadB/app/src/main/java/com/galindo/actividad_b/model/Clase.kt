package com.tecsupfit.app.model

data class Clase(
    val id: Int,
    val nombre: String,
    val categoria: String,       // "Hoy" o "Esta semana" -> usado por los chips de filtro
    val instructor: String,
    val horarios: List<String>   // mínimo 3 opciones de horario/cupo
)




