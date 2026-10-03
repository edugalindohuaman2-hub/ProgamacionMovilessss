
## Autor

Proyecto desarrollado como parte de un ejercicio de diseño e implementación de interfaz para una app de clínica médica.

## Licencia

Este proyecto se distribuye con fines educativos/demostrativos.

## mejora-ia promts

Prompt 1 - Flujo principal de agendamiento (Figura 1)
Crea una interfaz móvil en React con Tailwind para una app de clínica llamada "Clínica Salud+", con 4 pantallas en flujo:

1. Inicio: header morado con "Clínica Salud+ / Hola, Juan", tabs de especialidad (Cardiología, Pediatría) y una lista de médicos disponibles (nombre, especialidad, rating con estrella, icono +).
2. Perfil del médico: header con flecha "← Perfil del médico", ícono grande morado, nombre del doctor, especialidad y años de experiencia, rating, descripción breve, botón morado "Agendar cita".
3. Agendar cita: header "← Agendar cita", selector de fecha (3 días tipo chip, uno seleccionado en morado), selector de hora (chips), botón "Confirmar cita".
4. Confirmación: ícono de check verde, texto "¡Cita agendada!", resumen (doctor, fecha, hora), botón secundario "Ver mis citas". Usa tarjetas redondeadas, sombra suave, paleta morada (`#6B2FA5` aprox.) y tipografía limpia tipo Inter.

Commit: `feat: implementar flujo de agendamiento de citas (inicio, perfil médico, selección de fecha/hora, confirmación)`

Prompt 2 - Integración, navegación y responsive
Integra las pantallas del flujo de agendamiento (Prompt 1) con el menú lateral y "Mis citas" (Prompt 2) en una sola app de una página usando React Router o manejo de estado. Al confirmar una cita nueva, debe aparecer automáticamente en la lista de "Mis citas" con estado "Confirmada". Asegura que el layout sea responsive (mobile-first, max-width tipo celular ~375px centrado en pantallas grandes), con transiciones suaves entre pantallas y consistencia visual (colores, tipografía, espaciados) en toda la app.
Commit: `feat: integrar navegación entre flujo de citas y menú lateral, sincronizar estado de citas y ajustar diseño responsive`

Prompt 3 - Botón para ocultar/mostrar el menú lateral
Agrega un botón con ícono de flecha "←" en la esquina superior del menú lateral (drawer) de la app "Clínica Salud+". Al hacer clic en la flecha, el menú lateral debe ocultarse (colapsar con una transición suave de deslizamiento hacia la izquierda) y el contenido principal debe expandirse para ocupar el espacio disponible. Cuando el menú está oculto, muestra un botón flotante pequeño con ícono "→" en el borde izquierdo de la pantalla para volver a mostrarlo al hacer clic. Usa useState para manejar el estado isOpen/isCollapsed, con una transición CSS (transition-transform o transition-all) de unos 300ms para que el efecto sea fluido. Mantén la paleta morada y el estilo de bordes redondeados del resto de la app.
Commit: `feat: agregar botón toggle (flecha) para colapsar/expandir el menú lateral`