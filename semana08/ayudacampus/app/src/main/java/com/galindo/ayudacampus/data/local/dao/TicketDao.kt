package com.galindo.ayudacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.galindo.ayudacampus.data.local.entity.Comentario
import com.galindo.ayudacampus.data.local.entity.Ticket
import com.galindo.ayudacampus.data.local.relation.TicketConDetalle
import kotlinx.coroutines.flow.Flow

data class ConteoCategoria(
    val categoriaNombre: String,
    val total: Int
)

@Dao
interface TicketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTicket(t: Ticket): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarComentario(c: Comentario): Long

    @Query("UPDATE tickets SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String): Int

    @Query("UPDATE tickets SET prioridad = :prioridad WHERE id = :id")
    suspend fun actualizarPrioridad(id: Int, prioridad: String): Int

    @Query("UPDATE tickets SET estado = 'CERRADO', calificacion = :calificacion, fechaCierre = :fechaCierre WHERE id = :id")
    suspend fun cerrar(id: Int, calificacion: Int, fechaCierre: Long): Int

    @Query("UPDATE tickets SET estado = 'ABIERTO', calificacion = NULL, fechaCierre = NULL WHERE id = :id")
    suspend fun reabrir(id: Int): Int

    @Transaction
    @Query("SELECT * FROM tickets WHERE clienteId = :clienteId ORDER BY fecha DESC")
    fun observarPorCliente(clienteId: Int): Flow<List<TicketConDetalle>>

    @Transaction
    @Query("SELECT * FROM tickets ORDER BY CASE prioridad WHEN 'ALTA' THEN 0 WHEN 'MEDIA' THEN 1 WHEN 'BAJA' THEN 2 ELSE 3 END ASC, fecha DESC")
    fun observarTodos(): Flow<List<TicketConDetalle>>

    @Transaction
    @Query("SELECT * FROM tickets WHERE id = :id")
    fun observarUno(id: Int): Flow<TicketConDetalle?>

    @Query("SELECT COUNT(*) FROM tickets WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    @Query("SELECT c.nombre AS categoriaNombre, COUNT(t.id) AS total FROM categorias c LEFT JOIN tickets t ON c.id = t.categoriaId GROUP BY c.id, c.nombre")
    fun contarPorCategoria(): Flow<List<ConteoCategoria>>

    @Query("SELECT COUNT(*) FROM tickets WHERE prioridad = 'ALTA' AND (estado = 'ABIERTO' OR estado = 'PENDIENTE') AND fecha < :limite")
    fun contarAltasSinAtender(limite: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM tickets WHERE categoriaId = :categoriaId")
    suspend fun contarPorCategoriaId(categoriaId: Int): Int

    @Query("SELECT AVG(fechaCierre - fecha) FROM tickets WHERE estado = 'CERRADO' AND fechaCierre IS NOT NULL")
    fun obtenerTiempoPromedioResolucion(): Flow<Double?>
}
