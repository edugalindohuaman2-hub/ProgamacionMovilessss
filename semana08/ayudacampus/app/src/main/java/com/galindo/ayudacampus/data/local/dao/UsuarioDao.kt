package com.galindo.ayudacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.galindo.ayudacampus.data.local.entity.Usuario

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(lista: List<Usuario>): List<Long>

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int
}
