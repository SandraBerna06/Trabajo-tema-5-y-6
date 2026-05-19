# Documentación de Casos de Uso

---

# Caso de Uso 1: Crear Alarma

## Nombre
Crear Alarma

## Objetivo
Registrar una nueva alarma en el sistema con configuraciones de tiempo y etiqueta.

## Actor principal
Usuario

## Precondiciones
- El sistema debe estar inicializado.
- El `AlarmManager` debe estar operativo.

## Flujo principal
1. El usuario introduce la hora, etiqueta y categoría.
2. El sistema genera un identificador único (`UUID`).
3. El sistema asigna valores por defecto:
   - `isActive = true`
   - volumen = 50%
   - sonido = default
4. El sistema añade la alarma al `AlarmManager`.
5. El sistema confirma la creación al usuario.

## Flujos alternativos
- Ninguno.

## Postcondiciones
- La alarma queda guardada en la lista del sistema.

## Reglas de negocio
- Toda alarma nueva se inicializa con `isActive = true`.

---

# Caso de Uso 2: Eliminar Alarma

## Nombre
Eliminar Alarma

## Objetivo
Borrar permanentemente una alarma.

## Actor principal
Usuario

## Precondiciones
- Debe haber al menos una alarma registrada.

## Flujo principal
1. El usuario solicita eliminar una alarma por su ID.
2. El sistema busca la alarma.
3. El sistema elimina la alarma de la lista.
4. El sistema notifica el éxito.

## Flujos alternativos
### 2a. ID no encontrado
- El sistema no realiza ninguna acción y retorna `false`.

## Postcondiciones
- La alarma desaparece de la memoria del sistema.

## Reglas de negocio
- La eliminación es irreversible.

---

# Caso de Uso 3: Activar/Desactivar Alarma

## Nombre
Activar/Desactivar Alarma

## Objetivo
Cambiar el estado de ejecución de una alarma.

## Actor principal
Usuario

## Precondiciones
- La alarma debe existir previamente.

## Flujo principal
1. El usuario indica el ID y el nuevo estado.
2. El sistema localiza la alarma.
3. El sistema modifica el atributo `isActive`.
4. El sistema notifica el cambio.

## Flujos alternativos
### 2a. ID no encontrado
- El sistema no realiza cambios.

## Postcondiciones
- La alarma queda habilitada o deshabilitada para sonar.

## Reglas de negocio
- Las alarmas desactivadas son ignoradas en el cálculo de `getUpcomingAlarms`.

---

# Caso de Uso 4: Configurar Modo Vacaciones

## Nombre
Configurar Modo Vacaciones

## Objetivo
Pausar todas las alarmas globalmente.

## Actor principal
Usuario

## Precondiciones
- Ninguna.

## Flujo principal
1. El usuario solicita activar el modo vacaciones.
2. El sistema establece `vacationMode = true`.
3. El sistema notifica la pausa de alarmas.

## Flujos alternativos
### 1a. Desactivar modo vacaciones
- El sistema establece `vacationMode = false`.

## Postcondiciones
- Ninguna alarma sonará hasta desactivar el modo vacaciones.

## Reglas de negocio
- El modo vacaciones tiene prioridad sobre cualquier estado individual de alarma.

---

# Caso de Uso 5: Disparar Alarma

## Nombre
Disparar Alarma

## Objetivo
Ejecutar la alerta sonora al llegar el tiempo programado.

## Actor principal
Reloj del Sistema (`Timer`)

## Precondiciones
- `isActive = true`
- `vacationMode = false`
- El tiempo actual coincide con la hora programada.

## Flujo principal
1. El sistema detecta coincidencia temporal.
2. El sistema invoca `trigger()`.
3. El sistema imprime el aviso de sonido.
4. El sistema verifica si necesita reto matemático.

## Flujos alternativos
### 4a. Requiere reto matemático
1. El sistema instancia `MathChallenge`.
2. El sistema solicita respuesta al usuario.

## Postcondiciones
- La alarma entra en estado de alerta activa.

## Reglas de negocio
- El reto matemático bloquea la detención de la alarma si no se resuelve.

---

# Caso de Uso 6: Posponer Alarma (Snooze)

## Nombre
Posponer Alarma (Snooze)

## Objetivo
Retrasar la alerta 5 minutos.

## Actor principal
Usuario

## Precondiciones
- La alarma debe estar sonando.

## Flujo principal
1. El usuario solicita posponer la alarma.
2. El sistema calcula `tiempo_actual + 5 minutos`.
3. El sistema guarda el valor en `snoozedUntil`.
4. La alarma deja de sonar temporalmente.

## Flujos alternativos
- Ninguno.

## Postcondiciones
- La alarma volverá a dispararse al finalizar el tiempo de snooze.

## Reglas de negocio
- El tiempo de snooze prevalece sobre la hora base.

---

# Caso de Uso 7: Detener Alarma

## Nombre
Detener Alarma

## Objetivo
Apagar la alarma definitivamente o programar la siguiente repetición.

## Actor principal
Usuario

## Precondiciones
- La alarma debe estar sonando.

## Flujo principal
1. El usuario solicita detener la alarma.
2. El sistema verifica `requiresMathChallenge`.
3. Si aplica, se incluye el caso de uso **Resolver Reto Matemático**.
4. El sistema reinicia estados temporales.
5. El sistema programa la siguiente repetición o desactiva la alarma.

## Flujos alternativos
### 2a. Reto fallido
- La alarma continúa sonando.

## Postcondiciones
- La alarma queda en estado de reposo.

## Reglas de negocio
- Las alarmas sin repeticiones se desactivan automáticamente (`isActive = false`).

---

# Caso de Uso Incluido: Resolver Reto Matemático

## Nombre
Resolver Reto Matemático

## Objetivo
Validar la respuesta numérica al reto matemático.

## Actor principal
Usuario

## Precondiciones
- La alarma debe tener `requiresMathChallenge = true`.
- La alarma debe estar sonando.

## Flujo principal
1. El sistema recibe la respuesta del usuario.
2. El sistema compara con `correctAnswer`.
3. Si la respuesta es correcta:
   - retorna `true`
4. Si la respuesta es incorrecta:
   - retorna `false`

## Flujos alternativos
### 1a. Entrada inválida
- Se cuenta como fallo.

## Postcondiciones
- El sistema libera el objeto del reto matemático.

## Reglas de negocio
- El reto genera operandos aleatorios entre 1 y 20.