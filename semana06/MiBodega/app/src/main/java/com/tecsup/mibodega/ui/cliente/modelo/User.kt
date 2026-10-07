package com.tecsup.mibodega.ui.cliente.modelo

data class User(
    val email: String,
    val nombre: String,
    val telefono: String,
    val direccion: String
)

typealias Usuario = User
