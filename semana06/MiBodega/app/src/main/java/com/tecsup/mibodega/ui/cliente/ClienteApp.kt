package com.tecsup.mibodega.ui.cliente

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.*
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.categorias.CategoriasScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.favoritos.FavoritosScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.login.LoginScreen
import com.tecsup.mibodega.ui.cliente.screens.pedidos.PedidosScreen
import com.tecsup.mibodega.ui.cliente.screens.perfil.PerfilScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen
import com.tecsup.mibodega.ui.theme.BodegaTheme

@Composable
fun ClienteApp() {
    val navController = rememberNavController()

    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }
    var favoritosIds by remember { mutableStateOf<List<Int>>(emptyList()) }
    var pedidos by remember { mutableStateOf<List<Pedido>>(emptyList()) }
    var modoOscuro by remember { mutableStateOf(false) }

    var nombreUsuario by remember { mutableStateOf("") }
    var telefonoUsuario by remember { mutableStateOf("") }
    var passwordUsuario by remember { mutableStateOf("") }
    var direccionUsuario by remember { mutableStateOf("") }

    var subtotalTemp by remember { mutableStateOf(0.0) }
    var codigoUltimoPedido by remember { mutableStateOf("#1024") }
    var totalUltimoPedido by remember { mutableStateOf(0.0) }
    var direccionUltimoPedido by remember { mutableStateOf("") }

    BodegaTheme(darkTheme = modoOscuro) {
        NavHost(
            navController = navController,
            startDestination = "bienvenida",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            composable("bienvenida") {
                BienvenidaScreen(
                    onRegistrarse = { navController.navigate("registro") },
                    onIniciarSesion = { navController.navigate("login") },
                    onTerminos = {}
                )
            }
            composable("login") {
                LoginScreen(
                    usuarioRegistrado = telefonoUsuario,
                    passwordRegistrada = passwordUsuario,
                    onLoginExitoso = {
                        if (nombreUsuario.isBlank()) {
                            nombreUsuario = "Cliente Frecuente"
                            direccionUsuario = "Av. Principal 123"
                        }
                        navController.navigate("inicio/Todos") {
                            popUpTo("bienvenida") { inclusive = true }
                        }
                    },
                    onVolver = { navController.popBackStack() }
                )
            }
            composable("registro") {
                RegistroScreen(
                    onVolver = { navController.popBackStack() },
                    onCrearCuenta = { nombre, telefono, password, direccion, _ ->
                        nombreUsuario = nombre
                        telefonoUsuario = telefono
                        passwordUsuario = password
                        direccionUsuario = direccion
                        navController.navigate("inicio/Todos") {
                            popUpTo("bienvenida") { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = "inicio/{categoria}",
                arguments = listOf(navArgument("categoria") { type = NavType.StringType; defaultValue = "Todos" })
            ) { backStackEntry ->
                val categoria = backStackEntry.arguments?.getString("categoria") ?: "Todos"
                InicioScreen(
                    productos = listaProductosFake,
                    favoritosIds = favoritosIds,
                    cantidadCarrito = carrito.sumOf { it.cantidad },
                    categoriaInicial = categoria,
                    onVerCarrito = { navController.navigate("carrito") },
                    onToggleFavorito = { producto ->
                        favoritosIds = if (favoritosIds.contains(producto.id)) {
                            favoritosIds - producto.id
                        } else {
                            favoritosIds + producto.id
                        }
                    },
                    onAgregarCarrito = { producto ->
                        val existente = carrito.find { it.producto.id == producto.id }
                        carrito = if (existente != null) {
                            carrito.map { if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it }
                        } else {
                            carrito + ItemCarrito(producto, 1)
                        }
                    },
                    onProductoClick = { producto ->
                        navController.navigate("detalle/${producto.id}")
                    },
                    onNavegar = { ruta ->
                        if (ruta == "inicio") {
                            navController.navigate("inicio/Todos") {
                                popUpTo("inicio/Todos") { inclusive = true }
                            }
                        } else {
                            navController.navigate(ruta)
                        }
                    }
                )
            }
            composable(
                route = "detalle/{productoId}",
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productoId = backStackEntry.arguments?.getInt("productoId") ?: 1
                val producto = listaProductosFake.find { it.id == productoId } ?: listaProductosFake.first()
                val esFav = favoritosIds.contains(producto.id)

                DetalleProductoScreen(
                    producto = producto,
                    esFavorito = esFav,
                    onToggleFavorito = { prod ->
                        favoritosIds = if (favoritosIds.contains(prod.id)) {
                            favoritosIds - prod.id
                        } else {
                            favoritosIds + prod.id
                        }
                    },
                    onVolver = { navController.popBackStack() },
                    onAgregarAlCarrito = { prod, cant ->
                        val existente = carrito.find { it.producto.id == prod.id }
                        carrito = if (existente != null) {
                            carrito.map { if (it.producto.id == prod.id) it.copy(cantidad = it.cantidad + cant) else it }
                        } else {
                            carrito + ItemCarrito(prod, cant)
                        }
                        navController.popBackStack()
                    }
                )
            }
            composable("favoritos") {
                val favoritosLista = listaProductosFake.filter { favoritosIds.contains(it.id) }
                FavoritosScreen(
                    productosFavoritos = favoritosLista,
                    onToggleFavorito = { producto ->
                        favoritosIds = favoritosIds - producto.id
                    },
                    onAgregarCarrito = { producto ->
                        val existente = carrito.find { it.producto.id == producto.id }
                        carrito = if (existente != null) {
                            carrito.map { if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it }
                        } else {
                            carrito + ItemCarrito(producto, 1)
                        }
                    },
                    onProductoClick = { producto ->
                        navController.navigate("detalle/${producto.id}")
                    },
                    onNavegar = { ruta ->
                        if (ruta == "inicio") {
                            navController.navigate("inicio/Todos") {
                                popUpTo("inicio/Todos") { inclusive = true }
                            }
                        } else {
                            navController.navigate(ruta)
                        }
                    }
                )
            }
            composable("categorias") {
                CategoriasScreen(
                    onCategoriaClick = { categoria ->
                        navController.navigate("inicio/$categoria")
                    },
                    onNavegar = { ruta ->
                        if (ruta == "inicio") {
                            navController.navigate("inicio/Todos") {
                                popUpTo("inicio/Todos") { inclusive = true }
                            }
                        } else {
                            navController.navigate(ruta)
                        }
                    }
                )
            }
            composable("carrito") {
                subtotalTemp = carrito.sumOf { it.producto.precio * it.cantidad }
                CarritoScreen(
                    carrito = carrito,
                    onVolver = { navController.popBackStack() },
                    onIncrementar = { producto ->
                        carrito = carrito.map { if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it }
                    },
                    onDecrementar = { producto ->
                        carrito = carrito.mapNotNull {
                            if (it.producto.id == producto.id) {
                                if (it.cantidad > 1) it.copy(cantidad = it.cantidad - 1) else null
                            } else it
                        }
                    },
                    onEliminar = { producto ->
                        carrito = carrito.filterNot { it.producto.id == producto.id }
                    },
                    onVaciarCarrito = {
                        carrito = emptyList()
                    },
                    onContinuarPedido = {
                        navController.navigate("entrega")
                    }
                )
            }
            composable("entrega") {
                DatosEntregaScreen(
                    subtotal = subtotalTemp,
                    nombreInicial = nombreUsuario,
                    telefonoInicial = telefonoUsuario,
                    direccionInicial = direccionUsuario,
                    onVolver = { navController.popBackStack() },
                    onConfirmarPedido = { tipoEntrega, direccion, metodoPago, totalFinal ->
                        val codigo = "#${(1000..9999).random()}"
                        codigoUltimoPedido = codigo
                        totalUltimoPedido = totalFinal
                        direccionUltimoPedido = direccion

                        val nuevoPedido = Pedido(
                            id = codigo,
                            items = carrito,
                            total = totalFinal,
                            tipoEntrega = "$tipoEntrega ($metodoPago)",
                            direccion = direccion,
                            fecha = "02/10/2026"
                        )
                        pedidos = pedidos + nuevoPedido
                        carrito = emptyList()
                        navController.navigate("confirmacion") {
                            popUpTo("inicio/Todos")
                        }
                    }
                )
            }
            composable("confirmacion") {
                ConfirmacionScreen(
                    codigoPedido = codigoUltimoPedido,
                    totalPedido = totalUltimoPedido,
                    direccionPedido = direccionUltimoPedido,
                    onIrAInicio = {
                        navController.navigate("inicio/Todos") {
                            popUpTo("bienvenida") { inclusive = true }
                        }
                    },
                    onVerPedidos = {
                        navController.navigate("pedidos") {
                            popUpTo("inicio/Todos")
                        }
                    }
                )
            }
            composable("pedidos") {
                PedidosScreen(
                    pedidos = pedidos,
                    onNavegar = { ruta ->
                        if (ruta == "inicio") {
                            navController.navigate("inicio/Todos") {
                                popUpTo("inicio/Todos") { inclusive = true }
                            }
                        } else {
                            navController.navigate(ruta)
                        }
                    }
                )
            }
            composable("perfil") {
                PerfilScreen(
                    nombre = nombreUsuario,
                    telefono = telefonoUsuario,
                    direccion = direccionUsuario,
                    modoOscuro = modoOscuro,
                    onModoOscuroChange = { modoOscuro = it },
                    onCerrarSesion = {
                        nombreUsuario = ""
                        telefonoUsuario = ""
                        passwordUsuario = ""
                        direccionUsuario = ""
                        navController.navigate("bienvenida") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavegar = { ruta ->
                        if (ruta == "inicio") {
                            navController.navigate("inicio/Todos") {
                                popUpTo("inicio/Todos") { inclusive = true }
                            }
                        } else {
                            navController.navigate(ruta)
                        }
                    }
                )
            }
        }
    }
}
