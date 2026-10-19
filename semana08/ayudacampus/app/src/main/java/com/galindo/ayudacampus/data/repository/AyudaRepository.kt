package com.galindo.ayudacampus.data.repository

import com.galindo.ayudacampus.data.local.dao.ConteoCategoria
import com.galindo.ayudacampus.data.local.entity.Categoria
import com.galindo.ayudacampus.data.local.entity.Usuario
import com.galindo.ayudacampus.data.local.relation.TicketConDetalle
import kotlinx.coroutines.flow.Flow

interface AyudaRepository {
    suspend fun login(correo: String, clave: String): Usuario?

    suspend fun obtenerUsuarioPorId(id: Int): Usuario?

    suspend fun obtenerUsuarioPorRol(rol: String): Usuario?

    suspend fun crearTicket(
        clienteId: Int,
        categoriaId: Int,
        titulo: String,
        descripcion: String,
        ubicacion: String,
        prioridad: String
    ): Long

    suspend fun agregarComentario(ticketId: Int, autor: Usuario, texto: String): Long

    suspend fun cerrarConCalificacion(ticketId: Int, calificacion: Int): Int

    suspend fun reabrir(ticketId: Int): Int

    suspend fun cambiarEstado(ticketId: Int, estado: String): Int

    suspend fun cambiarPrioridad(ticketId: Int, prioridad: String): Int

    fun observarCategorias(): Flow<List<Categoria>>

    fun observarCategoriasActivas(): Flow<List<Categoria>>

    fun observarTicketsPorCliente(clienteId: Int): Flow<List<TicketConDetalle>>

    fun observarTodosLosTickets(): Flow<List<TicketConDetalle>>

    fun observarTicketPorId(ticketId: Int): Flow<TicketConDetalle?>

    fun contarTicketsPorEstado(estado: String): Flow<Int>

    fun contarTicketsPorCategoria(): Flow<List<ConteoCategoria>>

    fun contarAltasSinAtender(limite: Long): Flow<Int>

    suspend fun guardarCategoria(categoria: Categoria): Long

    suspend fun eliminarCategoria(categoria: Categoria): Int

    suspend fun contarTicketsPorCategoriaId(categoriaId: Int): Int
}
