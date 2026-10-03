package com.galindo.laboratorio.model

data class Student(
    val id: String,
    val name: String,
    val career: String,
    val email: String,
    val faculty: String,
    val bio: String,
    val phone: String = "+51 987 654 321",
    val cycle: String = "VI Ciclo",
    val isFeatured: Boolean = false
)

object StudentData {
    val sampleStudents = listOf(
        Student(
            id = "2024-0001",
            name = "Edu Galindo Huaman",
            career = "Ingeniería de Software",
            email = "edu.galindo@tecsup.edu.pe",
            faculty = "Ingeniería y Tecnología",
            bio = "Estudiante destacado con interés en desarrollo Android.",
            phone = "+51 987 654 321",
            cycle = "VI Ciclo",
            isFeatured = true
        ),
        Student(
            id = "2024-0002",
            name = "María García",
            career = "Arquitectura",
            email = "maria.garcia@tecsup.edu.pe",
            faculty = "Diseño y Arquitectura",
            bio = "Apasionada por el diseño sostenible y modelado 3D.",
            phone = "+51 912 345 678",
            cycle = "V Ciclo"
        ),
        Student(
            id = "2024-0003",
            name = "Carlos Perez",
            career = "Medicina",
            email = "carlos.perez@tecsup.edu.pe",
            faculty = "Ciencias de la Salud",
            bio = "Interesado en investigación biomédica y salud pública.",
            phone = "+51 923 456 789",
            cycle = "VII Ciclo"
        ),
        Student(
            id = "2024-0004",
            name = "Ana Lopez",
            career = "Derecho",
            email = "ana.lopez@tecsup.edu.pe",
            faculty = "Derecho y Ciencias Políticas",
            bio = "Enfocada en derecho corporativo y tecnología.",
            phone = "+51 934 567 890",
            cycle = "IV Ciclo"
        ),
        Student(
            id = "2024-0005",
            name = "Luis Ramirez",
            career = "Administración",
            email = "luis.ramirez@tecsup.edu.pe",
            faculty = "Gestión y Negocios",
            bio = "Emprendedor con enfoque en gestión de proyectos de innovación.",
            phone = "+51 945 678 901",
            cycle = "VIII Ciclo"
        )
    )

    fun getStudentById(id: String): Student {
        return sampleStudents.find { it.id == id } ?: sampleStudents.first()
    }
}
