package com.galindo.ayudacampus.data.local

import com.galindo.ayudacampus.data.local.entity.Categoria
import com.galindo.ayudacampus.data.local.entity.Comentario
import com.galindo.ayudacampus.data.local.entity.Ticket
import com.galindo.ayudacampus.data.local.entity.Usuario
import com.galindo.ayudacampus.data.model.EstadoTicket
import com.galindo.ayudacampus.data.model.Prioridad
import com.galindo.ayudacampus.data.model.Rol

object DatosIniciales {

    suspend fun sembrar(db: AyudaDatabase) {
        if (db.usuarioDao().contar() == 0) {
            // 1. Insertar Categorías
            val categorias = listOf(
                Categoria(nombre = "Hardware", descripcion = "Fallas físicas en monitores, teclados, PCs o proyectores"),
                Categoria(nombre = "Software", descripcion = "Instalación y errores de programas del campus"),
                Categoria(nombre = "Red / Wi-Fi", descripcion = "Problemas de conectividad a la red institucional"),
                Categoria(nombre = "Cuenta", descripcion = "Recuperación de contraseña y accesos a la plataforma"),
                Categoria(nombre = "Impresión", descripcion = "Inconvenientes con las impresoras de laboratorio y biblioteca")
            )
            db.categoriaDao().insertarTodos(categorias)

            // 2. Insertar Usuarios
            val estudiante = Usuario(
                nombre = "Juan Pérez",
                correo = "juan@campus.test",
                clave = "1234",
                rol = Rol.CLIENTE.name
            )
            val soporte = Usuario(
                nombre = "Soporte Técnico",
                correo = "soporte@campus.test",
                clave = "admin",
                rol = Rol.ADMIN.name
            )
            val usuarioIds = db.usuarioDao().insertarTodos(listOf(estudiante, soporte))
            val estudianteId = usuarioIds[0].toInt()
            val soporteId = usuarioIds[1].toInt()

            val horaActual = System.currentTimeMillis()
            fun horasAtras(h: Long) = horaActual - h * 3_600_000L

            // 3. Insertar 6 Tickets de ejemplo
            val ticket1Id = db.ticketDao().insertarTicket(
                Ticket(
                    clienteId = estudianteId,
                    categoriaId = 1,
                    titulo = "Proyector de Aula 201 no enciende",
                    descripcion = "El proyector parpadea en luz roja y no proyecta la imagen de la PC.",
                    ubicacion = "Pabellón A - Aula 201",
                    prioridad = Prioridad.ALTA.name,
                    estado = EstadoTicket.PENDIENTE.name,
                    fecha = horasAtras(2)
                )
            ).toInt()

            val ticket2Id = db.ticketDao().insertarTicket(
                Ticket(
                    clienteId = estudianteId,
                    categoriaId = 3,
                    titulo = "Sin conexión Wi-Fi en Biblioteca",
                    descripcion = "No puedo autenticarme en la red Wi-Fi CampusGuest desde mi laptop.",
                    ubicacion = "Biblioteca Principal",
                    prioridad = Prioridad.MEDIA.name,
                    estado = EstadoTicket.EN_PROCESO.name,
                    fecha = horasAtras(5)
                )
            ).toInt()

            val ticket3Id = db.ticketDao().insertarTicket(
                Ticket(
                    clienteId = estudianteId,
                    categoriaId = 4,
                    titulo = "Bloqueo de cuenta institucional",
                    descripcion = "Olvidé la clave y al tercer intento mi cuenta se bloqueó.",
                    ubicacion = "Laboratorio 3",
                    prioridad = Prioridad.ALTA.name,
                    estado = EstadoTicket.ATENDIDO.name,
                    fecha = horasAtras(24)
                )
            ).toInt()

            val ticket4Id = db.ticketDao().insertarTicket(
                Ticket(
                    clienteId = estudianteId,
                    categoriaId = 2,
                    titulo = "Error al abrir Android Studio en Lab 102",
                    descripcion = "Muestra error de falta de SDK al iniciar en la PC 15.",
                    ubicacion = "Laboratorio 102",
                    prioridad = Prioridad.BAJA.name,
                    estado = EstadoTicket.RESUELTO.name,
                    fecha = horasAtras(48),
                    fechaCierre = horasAtras(20),
                    calificacion = 5
                )
            ).toInt()

            val ticket5Id = db.ticketDao().insertarTicket(
                Ticket(
                    clienteId = estudianteId,
                    categoriaId = 5,
                    titulo = "Atasco de papel en Impresora Lab 2",
                    descripcion = "Quedó papel atascado en la bandeja de impresión secundaria.",
                    ubicacion = "Laboratorio 2",
                    prioridad = Prioridad.MEDIA.name,
                    estado = EstadoTicket.CANCELADO.name,
                    fecha = horasAtras(72),
                    fechaCierre = horasAtras(70)
                )
            ).toInt()

            val ticket6Id = db.ticketDao().insertarTicket(
                Ticket(
                    clienteId = estudianteId,
                    categoriaId = 1,
                    titulo = "Teclado dañado en PC 08",
                    descripcion = "Faltan las teclas Enter y Espacio en la PC 08 del laboratorio.",
                    ubicacion = "Laboratorio 101",
                    prioridad = Prioridad.BAJA.name,
                    estado = EstadoTicket.PENDIENTE.name,
                    fecha = horasAtras(1)
                )
            ).toInt()

            // 4. Insertar de 3 a 4 Comentarios de prueba
            db.ticketDao().insertarComentario(
                Comentario(
                    ticketId = ticket2Id,
                    autorId = soporteId,
                    autorNombre = "Soporte Técnico",
                    autorRol = Rol.ADMIN.name,
                    texto = "Estamos revisando el punto de acceso AP-Biblioteca-02.",
                    fecha = horasAtras(4)
                )
            )

            db.ticketDao().insertarComentario(
                Comentario(
                    ticketId = ticket2Id,
                    autorId = estudianteId,
                    autorNombre = "Juan Pérez",
                    autorRol = Rol.CLIENTE.name,
                    texto = "Gracias, quedo atento a la confirmación.",
                    fecha = horasAtras(3)
                )
            )

            db.ticketDao().insertarComentario(
                Comentario(
                    ticketId = ticket3Id,
                    autorId = soporteId,
                    autorNombre = "Soporte Técnico",
                    autorRol = Rol.ADMIN.name,
                    texto = "Se envió enlace de reseteo a su correo alternativo.",
                    fecha = horasAtras(22)
                )
            )

            db.ticketDao().insertarComentario(
                Comentario(
                    ticketId = ticket4Id,
                    autorId = soporteId,
                    autorNombre = "Soporte Técnico",
                    autorRol = Rol.ADMIN.name,
                    texto = "Se actualizó la ruta del SDK de Android en la PC 15 del Lab 102.",
                    fecha = horasAtras(21)
                )
            )
        }
    }
}
