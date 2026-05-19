# ⏰ Sistema de Despertador Inteligente (Lógica Core)

## 1. Descripción del proyecto
Este proyecto implementa la lógica de negocio subyacente para un sistema de alarmas inteligente. Diseñado sin interfaz gráfica para garantizar un desacoplamiento total, el sistema gestiona la creación, programación y estado de alarmas, calculando con precisión la próxima ejecución basándose en repeticiones semanales.

## 2. Objetivos
* Proveer un motor de alarmas robusto e independiente de la interfaz (desacoplamiento).
* Resolver la complejidad del cálculo temporal de alarmas recurrentes mediante la API `java.time`.
* Añadir funcionalidades avanzadas para evitar que el usuario se quede dormido (retos matemáticos) y facilitar la gestión masiva (modo vacaciones y categorización).

## 3. Tecnologías utilizadas
* **Lenguaje:** Java 17 (Uso exhaustivo de la API `java.time` y `Streams`).
* **Control de versiones:** Git y GitHub (Flujo de trabajo basado en Ramas/Pull Requests).
* **Diseño e Ingeniería:** Programación Orientada a Objetos (POO), diagramas UML (Mermaid), Markdown.

## 4. Instalación y ejecución
Al no disponer de interfaz gráfica, el sistema se ejecuta a través de una clase principal de simulación.
1. Clonar el repositorio: `git clone [URL_DEL_REPO]`
2. Navegar a la carpeta `src` y compilar los archivos: `javac *.java`
3. Ejecutar la simulación principal: `java Main`

## 5. Estructura del proyecto
```text
/
├── src/
│   ├── Alarm.java
│   ├── AlarmManager.java
│   ├── AlarmCategory.java
│   ├── MathChallenge.java
│   └── Main.java
├── docs/               (Diagramas y documentación adicional)
├── tests/              (Pruebas unitarias - futuras implementaciones)
└── README.md           (Documentación principal)