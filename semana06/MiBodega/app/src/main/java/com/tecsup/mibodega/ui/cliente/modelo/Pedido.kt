package com.tecsup.mibodega.ui.cliente.modelo

data class Pedido(
    val id: String,
    val items: List<ItemCarrito>,
    val total: Double,
    val tipoEntrega: String,
    val direccion: String,
    val fecha: String
)
