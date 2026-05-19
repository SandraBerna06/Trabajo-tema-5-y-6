# Justificación Técnica y Análisis de Diseño

Este documento profundiza en las decisiones de ingeniería adoptadas para el desarrollo del núcleo del sistema.

---

### Análisis de Requisitos Críticos
* **Representación de repetición semanal:** Se emplea `Set<DayOfWeek>`. Ventaja: Evita duplicados y permite consultas de tiempo constante $O(1)$ para saber si una alarma debe sonar hoy.
* **Evitar duplicidad de alarmas:** Se utiliza `UUID.randomUUID()` para cada instancia. Esto desvincula la identidad de la alarma de sus propiedades (hora/etiqueta), garantizando unicidad absoluta.
* **Gestión de sonidos:** Actualmente se modela mediante `String` (nombre del archivo). El diseño está preparado para evolucionar a una clase `SoundProfile` que encapsule metadatos de audio (volumen, tipo de archivo, fuente).
* **Implementación de Snooze:** Se utiliza un atributo de estado mutable `snoozedUntil`. Esta es la forma más limpia de "posponer" sin modificar la configuración base del usuario (la hora original).
* **Desacoplamiento:** Se ha aplicado el principio de responsabilidad única. 
    * `Alarm` solo gestiona sus propios datos.
    * `AlarmManager` gestiona la lógica de colecciones.
    * `Main` es solo un punto de entrada sin lógica de negocio, lo que permite testear las clases individualmente.

### Coherencia y Seguridad
* **Atributos:** Todos son `private`. Se accede mediante métodos (encapsulación).
* **Defensa de código:** Los *setters* incluyen lógica de filtrado (por ejemplo, el volumen se recorta entre 0 y 100 mediante `Math.max` y `Math.min`) para prevenir estados inválidos.

### Deuda Técnica y Futuras Mejoras
1. **Concurrencia:** Actualmente el sistema depende de un hilo síncrono. La mejora prioritaria es implementar `ScheduledExecutorService` para gestionar el tiempo de forma real e independiente.
2. **Persistencia:** Los datos residen en memoria volátil. Se recomienda la integración futura de JPA (Hibernate) o serialización JSON para persistir alarmas entre ejecuciones.

# Reflexión sobre el Uso de Inteligencia Artificial

Durante el desarrollo de este sistema, el uso de herramientas de IA generativa se ha integrado bajo un enfoque de asistencia crítica, utilizándola como un catalizador de productividad técnica y no como un reemplazo del juicio de ingeniería.

---

# ¿Cómo me ayudó?

## Aceleración de Boilerplate
La IA resultó indispensable para la generación automática de esqueletos de código repetitivo, como:

- Constructores
- Getters y setters
- Formateo de datos

Esto permitió enfocar el tiempo de desarrollo en la lógica de negocio central del sistema.

---

## Estructuración de Diagramas
La IA fue clave para transformar los requisitos funcionales en código sintáctico de Mermaid, facilitando la creación de:

- Diagramas de clases
- Diagramas de casos de uso

Sin esta ayuda, la curva de aprendizaje habría sido considerablemente más larga.

---

## Redacción Documental
La IA asistió en la creación de plantillas para los casos de uso, asegurando que se cumplieran correctamente todos los apartados exigidos por la rúbrica:

- Precondiciones
- Flujos principales
- Flujos alternativos
- Postcondiciones
- Reglas de negocio

---

# ¿Cuándo falló?

## Lógica de fechas recurrentes
La IA presentó una debilidad significativa al implementar el método `getNextTriggerTime`.

Inicialmente propuso una lógica basada en bucles que no contemplaba correctamente las alarmas de un solo uso (sin días de repetición), provocando:

- Ciclos infinitos
- Simulaciones temporales incorrectas
- Búsqueda indefinida del siguiente día válido

---

## Entendimiento del contexto
En algunas ocasiones, la IA sugirió:

- Frameworks externos
- Anotaciones innecesarias
- Librerías no permitidas

Esto entraba en conflicto con las restricciones técnicas de la práctica, cuyo objetivo era implementar una lógica core desacoplada y desarrollada únicamente con Java estándar.

---

# ¿Qué aprendí?

## La validación es innegociable
Aprendí que el código generado por IA debe considerarse una propuesta inicial y no un producto final listo para producción.

La supervisión humana es imprescindible para validar:

- Integridad de reglas de negocio
- Manejo correcto de estados
- Gestión de fechas y repeticiones
- Comportamientos límite

---

## Ingeniería de Prompts
También mejoré mi capacidad para proporcionar contexto técnico preciso.

Comprobé que cuanto más específicas son las restricciones indicadas a la IA, mejores resultados se obtienen.

Ejemplos:

- “Usa Java 17”
- “No utilices librerías externas”
- “Implementa únicamente lógica core”
- “Evita frameworks”

---

# ¿Qué validé manualmente?

## Pruebas de límites (Edge Cases)
Realicé simulaciones manuales con alarmas configuradas a las `23:59` para comprobar:

- El cambio correcto al día siguiente
- La ausencia de solapamientos
- El incremento temporal adecuado (`+1 día`)

---

## Inyección de excepciones
Forcé entradas no numéricas mediante `Scanner` durante el reto matemático para validar que:

- El bloque `try-catch` capturara correctamente las excepciones
- El hilo principal continuara ejecutándose sin interrupciones

---

## Integridad del Modo Vacaciones
Activé y desactivé repetidamente `vacationMode` para verificar que:

- `getUpcomingAlarms()` devolviera una lista vacía cuando el modo vacaciones estaba activo
- El sistema restaurara correctamente las alarmas al desactivarlo

---