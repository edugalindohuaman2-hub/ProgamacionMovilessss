package com.galindo.ayudacampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.galindo.ayudacampus.data.local.entity.Categoria
import com.galindo.ayudacampus.data.local.entity.Comentario
import com.galindo.ayudacampus.data.local.entity.Ticket
import com.galindo.ayudacampus.data.local.entity.Usuario

data class TicketConDetalle(
    @Embedded
    val ticket: Ticket,

    @Relation(
        parentColumn = "categoriaId",
        entityColumn = "id"
    )
    val categoria: Categoria,

    @Relation(
        parentColumn = "clienteId",
        entityColumn = "id"
    )
    val cliente: Usuario,

    @Relation(
        parentColumn = "id",
        entityColumn = "ticketId"
    )
    val comentarios: List<Comentario>
)
