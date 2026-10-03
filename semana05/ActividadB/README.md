# Actividad-B: TECSUP Fit

Aplicacion movil desarrollada en Android utilizando Jetpack Compose y el sistema de diseño Material 3, orientada a la gestion de reservas de clases y bienestar fisico en la institucion TECSUP. El proyecto implementa un diseño plano y vectorial basado en codigo, sin dependencias de fotografias o imagenes externas, garantizando un rendimiento optimo y una interfaz limpia y profesional.

---

## Caracteristicas Principales

- **Diseño Vectorial y Plano:** Todos los componentes visuales estan construidos mediante formas geometricas de Compose, iconos del sistema (`Icons.Default`) y gradientes institucionales.
- **Sistema de Colores Material 3:** Paleta personalizada con verde institucional profundo como color primario, contenedores tonales en verde menta claro y fondo gris neutro muy suave para un contraste adecuado.
- **Jerarquia Tipografica:** Definicion estricta de pesos tipograficos (Negrita para titulos, Mediano para subtitulos y Normal para detalles).
- **Navegacion Fluida:** Gestionada mediante `Jetpack Navigation Compose` con barra de navegacion inferior (`TecsupFitBottomBar`) con indicadores de seleccion activa en capsula tonal.

---

## Estructura de Pantallas

1. **Pantalla de Inicio (InicioScreen):**
   - Encabezado personalizado para el usuario (Edu Galindo, Plan Premium).
   - Buscador estilizado M3 para filtrado rapido por nombre o instructor.
   - Banner destacado y seccion de clases disponibles con filtros interactivos (`FilterChip`).
   - Tarjetas de clase (`OutlinedCard` / `ElevatedCard`) con bordes redondeados de 16.dp y etiquetas tipo pill (`AssistChip`) para salas y cupos.

2. **Pantalla de Detalle de Clase (DetalleClaseScreen):**
   - Caja contenedora vectorial principal (`Box`) con esquinas redondeadas y fondo secundario suave, centrando el icono vectorial de la disciplina.
   - Tarjeta de instructor con calificacion e informacion detallada (duracion, sala, nivel).
   - Selector interactivo de horarios disponibles con indicador de seleccion.

3. **Pantalla de Confirmacion (ConfirmacionScreen):**
   - Simulacion de boleto o ticket institucional mediante tarjeta centralizada con bordes definidos.
   - Icono de verificacion en contenedor circular menta.
   - Detalles organizados mediante separadores (`HorizontalDivider`) y codigo QR institucional vectorial.

4. **Pantalla de Mis Reservas (ReservasScreen):**
   - Historial de reservas institucionales organizadas en tarjetas con barra lateral vertical indicadora de estado (verde para Confirmada, gris para Completada).
   - Etiquetas de texto tonales (Badge) para resaltar el estado de cada pase.

5. **Pantalla de Mi Perfil (PerfilScreen):**
   - Avatar circular con iniciales en negrita (EG) y estado de membresia (Plan Premium).
   - Tarjetas metricas simetricas con numeros grandes en negrita para clases reservadas y rachas activas.
   - Seccion de informacion de usuario institucional (correo, codigo de estudiante y campus).

---

## Arquitectura y Datos

- **Modelos:**
  - `Clase`: Contiene identificador, nombre, categoria ("Hoy" o "Esta semana"), instructor, horarios disponibles, sala, duracion, cupos y descripcion.
  - `Reserva`: Vincula la clase seleccionada, el horario elegido y el estado actual ("Confirmada" o "Completada").
- **Datos de Muestra:** `SampleData` provee clases institucionales predefinidas como Yoga funcional, Cross Training y Spinning.

---

## Requisitos y Compilacion

- **IDE:** Android Studio (version Jellyfish o superior recomendada).
- **SDK de Android:** API 24 (minima) hasta API 34+ (objetivo).
- **Compilacion:** El proyecto utiliza Gradle y se puede compilar mediante la tarea de depuracion estandar:
  ```bash
  ./gradlew app:assembleDebug
  ```

---

## Prompts Utilizados para la Mejora de Interfaz (IA)

### Prompt 1: Pulido visual con Material Design 3 (Sin imagenes, solo componentes vectoriales)
- **Objetivo:** Elevar el estilo visual usando unicamentela paleta de colores, sombras sutiles, tipografia y componentes nativos de Jetpack Compose (`ElevatedCard`, `FilterChip`, vectores de Material).
- **Texto del prompt:**
  > "Rediseña la interfaz de las pantallas (Inicio, Detalle de clase y Confirmación) manteniéndote estrictamente dentro de un diseño plano y vectorial basado en código (sin fotografías ni imágenes reales).
  > Paleta de colores y componentes: Aplica un sistema de colores Material 3 con un verde institucional profundo como primario, contenedores tonales menta claro y un fondo gris neutro muy suave para dar contraste.
  > Tarjetas de clase: Sustituye las tarjetas planas por ElevatedCard o OutlinedCard con bordes redondeados (16.dp), aplicando sombras sutiles y bordes finos.
  > Iconografía y estado: Usa vectores del sistema (Icons.Default) para los contenedores de iconos. Agrega etiquetas tipo pill (AssistChip) para resaltar el cupo disponible o el número de sala con fondos de color tonal.
  > Jerarquía tipográfica: Define tipografías con pesos marcados (Bold para títulos, Medium para subtítulos y Normal para detalles) manteniendo una estructura visual clara sin recargar la pantalla."

### Prompt 2: Jerarquía de componentes y filtros limpios
- **Objetivo:** Reorganizar la estructura de las listas y la navegacion secundaria usando `FilterChip` y contenedores estructurados para mejorar el flujo tactil.
- **Texto del prompt:**
  > "Optimiza la distribución y arquitectura visual del flujo de reserva utilizando componentes vectoriales y Jetpack Compose puro.
  > Filtros de navegación: Reemplaza los botones de 'Hoy' y 'Esta semana' por un grupo horizontal de FilterChip estilizados con bordes suavizados y retroalimentación de color activa/inactiva.
  > Sección de Detalle: Diseña el área principal del detalle utilizando una caja contenedora vectorial (Box) con esquinas redondeadas y un fondo de color secundario suave, manteniendo el icono vectorial de la disciplina en el centro.
  > Confirmación de reserva: Diseña la pantalla de éxito simulando un boleto o ticket plano mediante un contenedor Card centralizado, con bordes definidos, un icono de verificación (CheckCircle) en un contenedor circular menta y detalles organizados con separadores (HorizontalDivider).
  > BottomBar: Asegura que los indicadores de la barra inferior utilicen sombras sutiles o cápsulas de selección alrededor del icono activo."

### Prompt 3: Dashboard de datos y tarjetas de estado para Reservas y Perfil
- **Objetivo:** Mejorar las pantallas de 'Mis reservas' y 'Mi perfil' utilizando tarjetas tonales, avatares con iniciales y badges de estado en codigo.
- **Texto del prompt:**
  > "Mejora el diseño de las pantallas secundarias 'Mis reservas' y 'Mi perfil' enfocándote en la presentación limpia de información sin usar imágenes externas.
  > Pantalla de Mis reservas: Transforma la lista de reservas en tarjetas con una barra vertical indicadora de estado en el borde izquierdo (verde para 'Confirmada', gris para 'Completada'). Incluye un Badge o etiqueta de texto dentro de cada tarjeta con colores de fondo tonales.
  > Pantalla de Perfil: Rediseña el avatar de usuario manteniendo un contenedor circular plano con las iniciales en negrita. Organiza las métricas ('Clases' y 'Rachas') dentro de dos tarjetas simétricas con elevación baja, números grandes en negrita y etiquetas pequeñas debajo.
  > Alineación y espaciado: Aplica un sistema de espaciado estándar (8.dp, 16.dp, 24.dp) entre cada componente para que la interfaz se sienta aireada, limpia y legible."
