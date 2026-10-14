package com.galindo.ayudacampus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comentarios")
data class Comentario(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
