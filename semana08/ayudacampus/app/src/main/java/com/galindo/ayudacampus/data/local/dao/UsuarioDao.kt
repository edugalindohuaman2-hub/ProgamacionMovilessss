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

    @Query("SELECT * FROM usuarios WHERE LOWER(correo) = LOWER(:correo) LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Usuario?

    @Query("SELECT * FROM usuarios WHERE UPPER(rol) = UPPER(:rol) LIMIT 1")
    suspend fun obtenerPorRol(rol: String): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(lista: List<Usuario>): List<Long>

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    @Query("UPDATE usuarios SET fotoUri = :fotoUri WHERE id = :id")
    suspend fun actualizarFotoPerfil(id: Int, fotoUri: String): Int
}
