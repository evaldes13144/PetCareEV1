# PetCare - Sistema de Gestión de Boxes

Aplicación de consola en Kotlin desarrollada para la gestión, control de estados y cobro de atenciones veterinarias, aplicando principios de Programación Orientada a Objetos (POO) y operaciones asíncronas con corrutinas.

## Requisitos Previos
* **Java Development Kit (JDK):** Versión 17 o 21.
* **Entorno de Desarrollo (IDE):** IntelliJ IDEA.
* **Gestor de dependencias:** Gradle (incluido en el proyecto).

## Instrucciones de Ejecución
1. Extrae el contenido del archivo ZIP en una carpeta local.
2. Abre IntelliJ IDEA y selecciona **Open**.
3. Selecciona la carpeta extraída (donde se encuentra el archivo `build.gradle.kts`).
4. Si el IDE lo solicita, selecciona **Load Gradle Project** o haz clic en el icono de sincronización de Gradle para descargar la dependencia de corrutinas (`kotlinx-coroutines-core`).
5. En el explorador del proyecto, navega a `src/main/kotlin/Main.kt`.
6. Haz clic en el botón de ejecución (flecha verde) ubicado a la izquierda de la línea `fun main() = runBlocking {`.
7. Selecciona **Run 'MainKt'** y visualiza la simulación y el reporte de cierre en la consola de la parte inferior.
