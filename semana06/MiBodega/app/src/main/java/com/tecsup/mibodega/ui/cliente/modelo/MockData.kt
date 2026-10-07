package com.tecsup.mibodega.ui.cliente.modelo

object MockData {
    val categorias = listOf("Todos", "Bebidas", "Abarrotes", "Snacks")

    val productos = listOf(
        Product(
            id = 1,
            nombre = "Arroz Costeño",
            peso = "750 g",
            descripcion = "Arroz extra, grano largo, ideal para el día a día.",
            precio = 4.50,
            categoria = "Abarrotes",
            imageUrl = "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400"
        ),
        Product(
            id = 2,
            nombre = "Aceite Primor",
            peso = "1 L",
            descripcion = "Aceite vegetal 1 L, alto en vitamina E.",
            precio = 8.90,
            categoria = "Abarrotes",
            imageUrl = "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=400"
        ),
        Product(
            id = 3,
            nombre = "Leche Gloria",
            peso = "400 g",
            descripcion = "Leche evaporada entera 400 g.",
            precio = 5.20,
            categoria = "Abarrotes",
            imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?w=400"
        ),
        Product(
            id = 4,
            nombre = "Galleta Oreo",
            peso = "126 g",
            descripcion = "Galletas de chocolate rellenas 126 g.",
            precio = 3.50,
            categoria = "Snacks",
            imageUrl = "https://images.unsplash.com/photo-1590080875515-8a3a8dc5735e?w=400"
        ),
        Product(
            id = 5,
            nombre = "Coca-Cola Original",
            peso = "1.5 L",
            descripcion = "Bebida gaseosa sabor cola. Ideal para compartir en familia.",
            precio = 6.50,
            categoria = "Bebidas",
            imageUrl = "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=400"
        )
    )
}

val listaProductosFake = MockData.productos
val listaCategorias = MockData.categorias
