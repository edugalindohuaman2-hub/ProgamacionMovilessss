# Mi Bodega - Aplicación Móvil de Abarrotes

Aplicación móvil desarrollada en **Jetpack Compose** para una tienda de abarrotes ("Mi Bodega"), implementada con buenas prácticas de arquitectura modular en UI, manejo de estados con Compose (`remember { mutableStateOf(...) }`), navegación estándar con `NavHost` y carga de imágenes mediante **Coil**.

---

## Características Principales

1. **Autenticación y Registro**: 
   - Validación de inicio de sesión con credenciales persistidas tras el registro (o usuario por defecto `999999999` / `123456`).
   - Validación de campos obligatorios (`isError = true`) en formularios de registro y entrega.
2. **Catálogo de Productos**:
   - Visualización de productos reales con imágenes dinámicas mediante `AsyncImage` (Coil).
   - Filtro por categorías (Todos, Bebidas, Abarrotes, Snacks) y barra de búsqueda en tiempo real.
   - Ordenamiento por precio (Menor a Mayor / Mayor a Menor) mediante chips interactivos.
   - Sistema de favoritos con marcado de corazón y pantalla dedicada.
3. **Gestión del Carrito**:
   - Control de cantidades (`-` / `+`), eliminación de ítems con confirmación por `AlertDialog`, opción de vaciar carrito y badge dinámico en la TopBar.
4. **Pasarela de Pago y Entrega**:
   - Selección de método de entrega y métodos de pago (`RadioButton` para **Efectivo**, **Yape** y **Plin**).
   - Cálculo automático de subtotal, costo de envío (S/ 4.00) y total.
5. **Confirmación y Seguimiento**:
   - Pantalla de éxito con comprobante y botón de consulta directa por WhatsApp al número **942164716**.
6. **Preferencias de Usuario**:
   - Modo Oscuro configurable desde la pantalla de Perfil con persistencia local en estado.
   - Pantalla de historial de "Mis pedidos".
   - Transiciones animadas entre pantallas en el `NavHost`.

---

# Mi Bodega - Rediseño de UI/UX con Inteligencia Artificial

Este documento registra el proceso de mejora de interfaz de usuario (UI/UX) para el proyecto **Mi Bodega** (Semana 06) en Jetpack Compose, utilizando asistentes de Inteligencia Artificial para emular un prototipo de 7 pantallas.

---

## Prompts Utilizados para la Mejora con IA

### Prompt 1: Bienvenida, Login y Registro de Datos (Pantallas 1 y 2)

**Objetivo:** Establecer el sistema de diseño visual (colores, formas) y construir el flujo inicial de autenticación e ingreso de datos.

> **Prompt enviado a la IA:**
> "Implementa en Jetpack Compose la pantalla de bienvenida/login y la pantalla de registro de datos para la app 'Mi Bodega', asegurando que incluya imágenes y vectores visuales:
> 1. **Configuración Visual:** Usa Coil (`AsyncImage`) o `Image` para mostrar el logo/ilustración de la bodega. Tema principal en verde esmeralda (`#2E7D32`), fondos neutros (`#F5F5F5` / `#FFFFFF`) y bordes redondeados (`RoundedCornerShape(16.dp)`).
> 2. **Pantalla 1 (Bienvenida/Login):** Logo superior, título 'Mi Bodega', eslogan 'Tus productos de siempre en la puerta de tu casa', botón primario verde 'Registrarme con mi teléfono', botón secundario 'Iniciar sesión' y términos de servicio.
> 3. **Pantalla 2 (Registro de Datos):** Header con botón de retorno, avatar de perfil circular con badge `+`, formulario `OutlinedTextField` para Nombre, Teléfono, Dirección y Referencia, finalizando con el botón 'Crear cuenta'."

---

### Prompt 2: Catálogo con Imágenes, Categorías y Detalle (Pantallas 3 y 4)

**Objetivo:** Desarrollar la exploración de productos, filtrado horizontal, grilla responsiva y vista detallada.

> **Prompt enviado a la IA:**
> "Desarrolla en Jetpack Compose el catálogo principal y la vista de detalle del producto incluyendo carga de imágenes mediante Coil (`AsyncImage`):
> 1. **Modelo de Datos Mock:** Crea una lista de prueba con `id`, `nombre`, `peso`, `precio`, `categoria` e `imageUrl` usando URLs públicas para productos reales (Coca-Cola, Arroz Costeño, Aceite Primor, Leche Gloria, Galleta Oreo).
> 2. **Pantalla 3 (Inicio/Productos):** TopBar con icono de carrito y Badge de notificación rojo, buscador redondeado 'Buscar productos...', chips horizontales de categorías ('Todos', 'Bebidas', 'Abarrotes', 'Snacks'), grilla de 2 columnas (`LazyVerticalGrid`) con tarjeta de producto (precio en rojo `#E53935` y botón `+` verde), y `NavigationBar` inferior de 4 opciones.
> 3. **Pantalla 4 (Detalle del Producto):** Imagen principal centrada, título, presentación (1.5 L), precio destacado, selector horizontal de cantidad (`- 1 +`) y botón inferior 'Agregar al carrito'."

---

### Prompt 3: Carrito, Métodos de Pago y Confirmación (Pantallas 5, 6 y 7)

**Objetivo:** Construir el flujo completo de Checkout, selección de pasarela de pago y estado final del pedido.

> **Prompt enviado a la IA:**
> "Construye en Jetpack Compose el flujo final de compras (Carrito, Pasarela de Pago y Pantalla de Éxito) renderizando miniaturas de productos e iconos:
> 1. **Pantalla 5 (Mi Carrito):** Header con tacho de basura para vaciar, lista `LazyColumn` de items con miniatura (`AsyncImage`), precio, control de cantidad y opción de eliminar. Resumen con Subtotal, Delivery y Total destacado, finalizando con botón 'Continuar pedido'.
> 2. **Pantalla 6 (Datos de Entrega y Pago):** Formulario prellenado de envío y sección de 'Método de pago' con `RadioButton` para Efectivo al entregar, Yape y Plin con sus respectivos logos/iconos. Botón 'Confirmar pedido'.
> 3. **Pantalla 7 (Pedido Confirmado):** Icono circular grande de Check verde, mensaje '¡Pedido realizado!', resumen con código `#1024`, Total a pagar y dirección. Botones 'Ver estado del pedido' y 'Volver al inicio'."
