package com.galindo.ayudacampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.galindo.ayudacampus.data.local.dao.CategoriaDao
import com.galindo.ayudacampus.data.local.dao.TicketDao
import com.galindo.ayudacampus.data.local.dao.UsuarioDao
import com.galindo.ayudacampus.data.local.entity.Categoria
import com.galindo.ayudacampus.data.local.entity.Comentario
import com.galindo.ayudacampus.data.local.entity.Ticket
import com.galindo.ayudacampus.data.local.entity.Usuario

@Database(
    entities = [
        Categoria::class,
        Usuario::class,
        Ticket::class,
        Comentario::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class AyudaDatabase : RoomDatabase() {

    abstract fun categoriaDao(): CategoriaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun ticketDao(): TicketDao

    companion object {
        @Volatile
        private var INSTANCE: AyudaDatabase? = null

        fun getDatabase(context: Context): AyudaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AyudaDatabase::class.java,
                    "ayudacampus.db",
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
