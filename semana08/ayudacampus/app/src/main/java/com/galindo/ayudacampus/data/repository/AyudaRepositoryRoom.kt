package com.galindo.ayudacampus.data.repository

import com.galindo.ayudacampus.data.local.dao.CategoriaDao
import com.galindo.ayudacampus.data.local.dao.ConteoCategoria
import com.galindo.ayudacampus.data.local.dao.TicketDao
import com.galindo.ayudacampus.data.local.dao.UsuarioDao
import com.galindo.ayudacampus.data.local.entity.Categoria
import com.galindo.ayudacampus.data.local.entity.Comentario
import com.galindo.ayudacampus.data.local.entity.Ticket
import com.galindo.ayudacampus.data.local.entity.Usuario
import com.galindo.ayudacampus.data.local.relation.TicketConDetalle
import kotlinx.coroutines.flow.Flow

class AyudaRepositoryRoom(
    private val usuarioDao: UsuarioDao,
    private val categoriaDao: CategoriaDao,
    private val ticketDao: TicketDao
) : AyudaRepository {

    override suspend fun login(correo: String, clave: String): Usuario? {
        return usuarioDao.login(correo, clave)
    }

    override suspend fun obtenerUsuarioPorId(id: Int): Usuario? {
        return usuarioDao.obtenerPorId(id)
    }

    override suspend fun obtenerUsuarioPorRol(rol: String): Usuario? {
        return usuarioDao.obtenerPorRol(rol)
    }

    override suspend fun crearTicket(
        clienteId: Int,
        categoriaId: Int,
        titulo: String,
        descripcion: String,
        ubicacion: String,
        prioridad: String
    ): Long {
        val nuevoTicket = Ticket(
            clienteId = clienteId,
            categoriaId = categoriaId,
            titulo = titulo,
            descripcion = descripcion,
            ubicacion = ubicacion,
            prioridad = prioridad,
            estado = "ABIERTO",
            fecha = System.currentTimeMillis()
        )
        return ticketDao.insertarTicket(nuevoTicket)
    }

    override suspend fun agregarComentario(ticketId: Int, autor: Usuario, texto: String): Long {
        val nuevoComentario = Comentario(
            ticketId = ticketId,
            autorId = autor.id,
            autorNombre = autor.nombre,
            autorRol = autor.rol,
            texto = texto,
            fecha = System.currentTimeMillis()
        )
        return ticketDao.insertarComentario(nuevoComentario)
    }

    override suspend fun cerrarConCalificacion(ticketId: Int, calificacion: Int): Int {
        val fechaCierre = System.currentTimeMillis()
        return ticketDao.cerrar(ticketId, calificacion, fechaCierre)
    }

    override suspend fun reabrir(ticketId: Int): Int {
        return ticketDao.reabrir(ticketId)
    }

    override suspend fun cambiarEstado(ticketId: Int, estado: String): Int {
        return ticketDao.actualizarEstado(ticketId, estado)
    }

    override suspend fun cambiarPrioridad(ticketId: Int, prioridad: String): Int {
        return ticketDao.actualizarPrioridad(ticketId, prioridad)
    }

    override fun observarCategorias(): Flow<List<Categoria>> {
        return categoriaDao.observarTodas()
    }

    override fun observarCategoriasActivas(): Flow<List<Categoria>> {
        return categoriaDao.observarCategoriasActivas()
    }

    override fun observarTicketsPorCliente(clienteId: Int): Flow<List<TicketConDetalle>> {
        return ticketDao.observarPorCliente(clienteId)
    }

    override fun observarTodosLosTickets(): Flow<List<TicketConDetalle>> {
        return ticketDao.observarTodos()
    }

    override fun observarTicketPorId(ticketId: Int): Flow<TicketConDetalle?> {
        return ticketDao.observarUno(ticketId)
    }

    override fun contarTicketsPorEstado(estado: String): Flow<Int> {
        return ticketDao.contarPorEstado(estado)
    }

    override fun contarTicketsPorCategoria(): Flow<List<ConteoCategoria>> {
        return ticketDao.contarPorCategoria()
    }

    override fun contarAltasSinAtender(limite: Long): Flow<Int> {
        return ticketDao.contarAltasSinAtender(limite)
    }

    override suspend fun guardarCategoria(categoria: Categoria): Long {
        return categoriaDao.insertar(categoria)
    }

    override suspend fun eliminarCategoria(categoria: Categoria): Int {
        return categoriaDao.eliminar(categoria)
    }

    override suspend fun contarTicketsPorCategoriaId(categoriaId: Int): Int {
        return ticketDao.contarPorCategoriaId(categoriaId)
    }

    override fun obtenerTiempoPromedioResolucion(): Flow<Double?> {
        return ticketDao.obtenerTiempoPromedioResolucion()
    }

    override suspend fun actualizarFotoPerfil(usuarioId: Int, fotoUri: String) {
        usuarioDao.actualizarFotoPerfil(usuarioId, fotoUri)
    }
}
