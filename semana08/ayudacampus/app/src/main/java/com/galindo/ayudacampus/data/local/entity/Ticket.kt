package com.galindo.ayudacampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.galindo.ayudacampus.data.model.EstadoTicket
import com.galindo.ayudacampus.data.model.Prioridad

@Entity(
    tableName = "tickets",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["clienteId"]),
        Index(value = ["categoriaId"])
    ]
)
data class Ticket(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clienteId: Int,
    val categoriaId: Int,
    val titulo: String,
    val descripcion: String,
    val ubicacion: String,
    val prioridad: String = Prioridad.BAJA.name,
    val estado: String = EstadoTicket.PENDIENTE.name,
    val fecha: Long = System.currentTimeMillis(),
    val fechaCierre: Long? = null,
    val calificacion: Int? = null
)
