# Registro de Prompts de Inteligencia Artificial - Proyecto AyudaCampus

- **Proyecto:** AyudaCampus - Sistema de Gestión de Incidencias TI
- **Repositorio:** https://github.com/edugalindohuaman2-hub/ProgamacionMovilessss.git
- **Arquitectura:** MVVM + Repository Pattern (Room, Kotlin Coroutines, Jetpack Compose & Material 3)

---

## Registro de Prompts Utilizados

### Prompt 1: Tiempo Promedio de Resolución de Tickets
* **Texto del Prompt enviado a la IA:**
  > "Actúa como un Arquitecto de Software Android Senior especializado en Kotlin, Jetpack Compose, Material 3 y Room.
  > Contexto: Aplicación Android bajo arquitectura estricta MVVM + Repository (Interface). Tabla Room 'tickets' con columnas 'fecha' (Long millis) y 'fechaCierre' (Long? millis), 'estado' (String).
  > Requerimiento: Implementa el cálculo y la visualización del tiempo promedio de resolución de los tickets cerrados (`fechaCierre - fecha`) en el Panel de Soporte TI.
  > Capas: TicketDao -> AyudaRepository (interface) -> AyudaRepositoryRoom -> PanelViewModel -> PanelScreen."

* **Qué generó la IA:**
  - Consulta `@Query("SELECT AVG(fechaCierre - fecha) FROM tickets WHERE estado = 'CERRADO' AND fechaCierre IS NOT NULL")` en `TicketDao` retornando `Flow<Double?>`.
  - Método observacional en `AyudaRepository` y su implementación en `AyudaRepositoryRoom`.
  - Transformación del promedio en milisegundos a formato humano (días, horas, minutos o "Sin datos") usando `StateFlow<String>` en `PanelViewModel`.
  - Componente de interfaz `Card` en `PanelScreen` alineado con Material 3 y un icono representativo de reloj (`Icons.Default.Schedule`).

* **Ajustes y Correcciones Manuales:**
  - Se validaron los casos borde cuando no existen tickets cerrados en la base de datos (retorno `null` de SQLite `AVG()`), asegurando que muestre la cadena `"Sin datos"` o `"< 1 min"` sin generar excepciones por valores nulos.
  - Se ajustó el formato de las cadenas para corregir redundancias en plantillas de formato de Kotlin (removiendo llaves innecesarias).

* **Evaluación del Respeto al Patrón MVVM:**
  - **Cumple: SÍ.**
  - **Razón:** La View (`PanelScreen`) interactúa únicamente con el `PanelViewModel` a través de `StateFlow<String>`. La View no conoce `TicketDao`, consultas SQL ni la entidad de base de datos. La capa de datos responde reactivamente a través de `Flow` en el repositorio.

---

### Prompt 2: Indicador Visual de Cumplimiento de SLA en Tarjetas
* **Texto del Prompt enviado a la IA:**
  > "Actúa como un Desarrollador Android Senior experto en UI/UX con Jetpack Compose y estado reactivo.
  > Contexto: App de Helpdesk con tickets en Room (prioridades: ALTA, MEDIA, BAJA; campo fecha Long millis creación).
  > Reglas SLA: ALTA (4h), MEDIA (24h), BAJA (72h).
  > Requerimiento: Agrega un indicador visual de cumplimiento de SLA (tiempo restante o 'SLA Vencido') en cada tarjeta de ticket (`TicketCard`).
  > Componentes: Estimador SLA en milisegundos y Badge dinámico en Compose (Verde/Azul: Dentro del margen, Ámbar: <1 hora, Rojo: Vencido)."

* **Qué generó la IA:**
  - Función de cálculo de SLA `calcularSLA()` en Kotlin puro basada en la fecha actual (`System.currentTimeMillis()`) y los límites por prioridad.
  - Modelo de datos liviano `EstadoSLA` para encapsular el texto explicativo y los colores de fondo/texto.
  - Badge/Chip dentro de `TicketCard` integrado en el encabezado junto a los chips de Prioridad y Estado.

* **Ajustes y Correcciones Manuales:**
  - Se condicionó el cálculo de SLA para que se ignore automáticamente en tickets que ya han sido cerrados, resueltos o cancelados, evitando mostrar mensajes de vencimiento descontextualizados.
  - Se optimizaron las paletas de colores asociadas a cada rango según la guía de diseño de Material 3 para mejorar la legibilidad y contraste.

* **Evaluación del Respeto al Patrón MVVM:**
  - **Cumple: SÍ.**
  - **Razón:** El cálculo es puramente declarativo e inmutable, derivado de las propiedades del modelo expuesto por el ViewModel (`TicketConDetalle`). La View no modifica datos de persistencia ni viola el flujo unidireccional de datos (UDF).

---

### Prompt 3: Búsqueda y Filtrado Avanzado Multi-criterio
* **Texto del Prompt enviado a la IA:**
  > "Actúa como un Desarrollador Android Principal experto en Kotlin Coroutines y Flow combination.
  > Contexto: Bandeja de Soporte (`BandejaScreen` / `BandejaViewModel`).
  > Requerimiento: Implementa un sistema de búsqueda y filtrado avanzado multi-criterio en tiempo real por texto libre (título, descripción, cliente), prioridad (Todas, Alta, Media, Baja) y categoría.
  > Instrucciones: Usar `combine(...)` sobre los StateFlows en `BandejaViewModel` para consolidar un único `StateFlow<List<TicketConDetalle>>`. Diseñar barra de búsqueda y barra de chips en Compose."

* **Qué generó la IA:**
  - Declaración de `StateFlows` reactivos para la consulta de búsqueda (`_busqueda`), filtro de prioridad (`_filtroPrioridad`) y categoría (`_filtroCategoria`).
  - Combinación reactiva con `combine(...)` de 5 flujos en `BandejaViewModel`, filtrando la lista de manera reactiva en memoria sin ejecutar consultas SQL adicionales.
  - Componente UI en `BandejaScreen` con `OutlinedTextField` (con icono de búsqueda y botón de limpieza) y filas horizontales scrollables de `FilterChip`.

* **Ajustes y Correcciones Manuales:**
  - Se aseguró que la búsqueda por texto ignore mayúsculas y minúsculas (`ignoreCase = true`) y elimine espacios vacíos extremos (`trim()`).
  - Se garantizó la reactividad completa al cargar las categorías dinámicamente desde el repositorio para poblar los chips de selección.

* **Evaluación del Respeto al Patrón MVVM:**
  - **Cumple: SÍ.**
  - **Razón:** El ViewModel administra todo el estado de los filtros y expone únicamente una lista lista para ser consumida (`StateFlow<List<TicketConDetalle>>`). La pantalla `BandejaScreen` solo delega los eventos de interacción del usuario mediante callbacks (`onBusquedaCambiada`, `onPrioridadSeleccionada`, etc.).

---

## Reflexión Final

El uso de asistentes de Inteligencia Artificial agilizó significativamente la generación de código boilerplate en Jetpack Compose, consultas SQL en Room y operadores reactivos complejos de Kotlin Coroutines (`combine`, `map`, `stateIn`). Sin embargo, el proceso requirió supervisión humana continua para garantizar que el código producido se integrara armoniosamente con las clases existentes sin romper contratos previamente definidos. 

Durante las iteraciones, la IA tendió en ocasiones a proponer soluciones que duplicaban lógica entre componentes o que importaban iconos obsoletos. La intervención técnica permitió corregir estos detalles, asegurando el cumplimiento de los principios de Clean Architecture y la separación estricta de responsabilidades en MVVM. Como aprendizaje clave, se reforzó la importancia del uso de los operadores de Kotlin Flow para procesar transformaciones de datos en la capa ViewModel de forma reactiva y eficiente, evitando recargar la base de datos Room con consultas SQL redundantes y logrando una interfaz de usuario completamente fluida y adaptativa con Material 3.
