package com.tecsup.mibodega.ui.cliente

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.mibodega.ui.cliente.modelo.*
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.categorias.CategoriasScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
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

    var subtotalTemp by remember { mutableStateOf(0.0) }

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
                    onLoginExitoso = {
                        navController.navigate("inicio") {
                            popUpTo("bienvenida") { inclusive = true }
                        }
                    },
                    onVolver = { navController.popBackStack() }
                )
            }
            composable("registro") {
                RegistroScreen(
                    onVolver = { navController.popBackStack() },
                    onCrearCuenta = { _, _, _, _ ->
                        navController.navigate("inicio") {
                            popUpTo("bienvenida") { inclusive = true }
                        }
                    }
                )
            }
            composable("inicio") {
                InicioScreen(
                    productos = listaProductosFake,
                    favoritosIds = favoritosIds,
                    cantidadCarrito = carrito.sumOf { it.cantidad },
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
                    onNavegar = { ruta ->
                        if (ruta != "inicio") navController.navigate(ruta)
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
                    onNavegar = { ruta ->
                        if (ruta != "favoritos") navController.navigate(ruta)
                    }
                )
            }
            composable("categorias") {
                CategoriasScreen(
                    onCategoriaClick = { _ ->
                        navController.navigate("inicio")
                    },
                    onNavegar = { ruta ->
                        if (ruta != "categorias") navController.navigate(ruta)
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
                    onContinuarPedido = {
                        navController.navigate("entrega")
                    }
                )
            }
            composable("entrega") {
                DatosEntregaScreen(
                    subtotal = subtotalTemp,
                    onVolver = { navController.popBackStack() },
                    onConfirmarPedido = { tipoEntrega, direccion, totalFinal ->
                        val nuevoPedido = Pedido(
                            id = (pedidos.size + 1).toString(),
                            items = carrito,
                            total = totalFinal,
                            tipoEntrega = tipoEntrega,
                            direccion = direccion,
                            fecha = "02/10/2026"
                        )
                        pedidos = pedidos + nuevoPedido
                        carrito = emptyList()
                        navController.navigate("confirmacion") {
                            popUpTo("inicio")
                        }
                    }
                )
            }
            composable("confirmacion") {
                ConfirmacionScreen(
                    onIrAInicio = {
                        navController.navigate("inicio") {
                            popUpTo("bienvenida") { inclusive = true }
                        }
                    },
                    onVerPedidos = {
                        navController.navigate("pedidos") {
                            popUpTo("inicio")
                        }
                    }
                )
            }
            composable("pedidos") {
                PedidosScreen(
                    pedidos = pedidos,
                    onNavegar = { ruta ->
                        if (ruta != "pedidos") navController.navigate(ruta)
                    }
                )
            }
            composable("perfil") {
                PerfilScreen(
                    modoOscuro = modoOscuro,
                    onModoOscuroChange = { modoOscuro = it },
                    onCerrarSesion = {
                        navController.navigate("bienvenida") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavegar = { ruta ->
                        if (ruta != "perfil") navController.navigate(ruta)
                    }
                )
            }
        }
    }
}
