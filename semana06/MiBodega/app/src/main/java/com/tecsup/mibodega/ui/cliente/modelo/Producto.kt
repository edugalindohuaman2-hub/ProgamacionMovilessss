package com.tecsup.mibodega.ui.cliente.modelo

data class Producto(
    val id: Int,
    val nombre: String,
    val peso: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val imageUrl: String
)
