package com.galindo.ayudacampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.galindo.ayudacampus.data.local.entity.Ticket

data class TicketConDetalle(
    @Embedded
    val ticket: Ticket
)
