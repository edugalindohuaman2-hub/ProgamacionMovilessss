# Laboratorio 03 - Registro de Producto

## Mejora con IA

### Descripción

En la Parte B del laboratorio se realizó una mejora al proyecto utilizando inteligencia artificial. Todo el trabajo se desarrolló en la rama `mejora-ia`, sin modificar la rama `main`.

La mejora consistió en agregar validación de campos vacíos y un botón **LIMPIAR** para reiniciar el formulario.

## Prompt que usé

En mi proyecto de Android Studio con Jetpack Compose, modifica únicamente el composable `PantallaRegistro`.

Quiero agregar una validación de campos vacíos: cuando presione el botón **AGREGAR PRODUCTO** y falte algún dato (nombre, precio o cantidad), no debe aparecer la Card. En su lugar, debe mostrarse un mensaje de error en color rojo indicando que complete todos los campos.

También agrega un botón **LIMPIAR** que vacíe los campos de nombre, precio y cantidad y oculte el resumen y el mensaje de error.

No cambies el diseño general, los nombres de los campos ni la lógica del cálculo del importe. Mantén el código compatible con Jetpack Compose y asegúrate de que compile.

## Mejora con IA

| Prompt que usé                                                             | Qué generó la IA                                                                                                          | Qué acepté o corregí                                                                                                  |
| -------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------- |
| Agregar validación de campos vacíos y mostrar un mensaje de error en rojo. | Se agregó una validación para comprobar si el nombre, precio o cantidad estaban vacíos antes de mostrar la Card.          | Revisé la validación y comprobé que el resumen no aparezca cuando falta algún dato.                                   |
| Agregar un botón **LIMPIAR** para borrar los datos del formulario.         | Se agregó un botón que reinicia los campos de nombre, precio y cantidad. También oculta el resumen y el mensaje de error. | Probé el botón y verifiqué que todos los campos queden vacíos correctamente.                                          |
| Mantener el diseño y la lógica original del proyecto.                      | Se mantuvo la estructura principal de `PantallaRegistro` y el cálculo del importe.                                        | Revisé el código y realicé pequeños ajustes para que la mejora funcione correctamente sin afectar el diseño original. |

## Pruebas realizadas

* Se probó presionar **AGREGAR PRODUCTO** con campos vacíos.
* Se verificó que aparezca un mensaje de error en color rojo.
* Se ingresaron datos válidos en todos los campos.
* Se comprobó que aparezca la Card con el resumen del producto.
* Se probó ingresar letras en el precio para verificar que la aplicación no se cierre.
* Se presionó el botón **LIMPIAR**.
* Se verificó que los campos, el resumen y el mensaje de error se reinicien correctamente.

## Resultado

La mejora permite validar que el usuario complete los campos antes de registrar un producto. Además, el botón **LIMPIAR** facilita reiniciar el formulario para ingresar un nuevo producto.

## Rama utilizada

`mejora-ia`

## Commits de la Parte B

1. `Aplica mejora generada con IA: validacion y boton limpiar`
2. `Corrige codigo de la IA: mejora validacion y limpieza`
3. `Documenta prompts y decisiones en README`
