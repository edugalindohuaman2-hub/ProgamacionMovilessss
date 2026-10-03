package com.tecsupfit.app.data

import com.tecsupfit.app.model.Clase

object SampleData {
    val clases = listOf(
        Clase(
            id = 1,
            nombre = "Yoga Flow",
            categoria = "Hoy",
            instructor = "Ana Torres",
            horarios = listOf("7:00 am", "9:00 am", "6:00 pm")
        ),
        Clase(
            id = 2,
            nombre = "Spinning Extremo",
            categoria = "Hoy",
            instructor = "Luis Paredes",
            horarios = listOf("6:00 am", "12:00 pm", "7:00 pm")
        ),
        Clase(
            id = 3,
            nombre = "Funcional HIIT",
            categoria = "Esta semana",
            instructor = "Carla Ruiz",
            horarios = listOf("Lun 8:00 am", "Mié 8:00 am", "Vie 8:00 am")
        ),
        Clase(
            id = 4,
            nombre = "Crossfit Básico",
            categoria = "Esta semana",
            instructor = "Diego Salas",
            horarios = listOf("Mar 7:00 pm", "Jue 7:00 pm", "Sáb 10:00 am")
        ),
        Clase(
            id = 5,
            nombre = "Pilates Reformer",
            categoria = "Hoy",
            instructor = "Marisol Vega",
            horarios = listOf("9:00 am", "11:00 am", "5:00 pm")
        )
    )
}
