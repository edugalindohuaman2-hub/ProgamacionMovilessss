package com.galindo.ayudacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galindo.ayudacampus.data.local.entity.Categoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(categoria: Categoria): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(lista: List<Categoria>): List<Long>

    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun observarTodas(): Flow<List<Categoria>>

    @Query("SELECT COUNT(*) FROM categorias")
    suspend fun contar(): Int
}
