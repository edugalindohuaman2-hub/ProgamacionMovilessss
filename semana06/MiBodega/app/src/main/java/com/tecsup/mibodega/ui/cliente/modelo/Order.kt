package com.tecsup.mibodega.ui.cliente.modelo

data class Order(
    val id: String,
    val items: List<CartItem>,
    val total: Double,
    val tipoEntrega: String,
    val direccion: String,
    val fecha: String
)
