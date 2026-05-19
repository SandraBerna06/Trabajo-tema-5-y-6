import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AlarmManager manager = new AlarmManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== INICIANDO SISTEMA DE DESPERTADOR INTELIGENTE ===\n");

        // ---------------------------------------------------------
        // 1. CREACIÓN DE ALARMAS (Básico + Categorías)
        // ---------------------------------------------------------
        Alarm workAlarm = new Alarm(LocalTime.of(7, 0), "Despertar Trabajo", AlarmCategory.TRABAJO);
        workAlarm.addRepeatDay(DayOfWeek.MONDAY);
        workAlarm.addRepeatDay(DayOfWeek.TUESDAY);
        workAlarm.addRepeatDay(DayOfWeek.WEDNESDAY);
        workAlarm.addRepeatDay(DayOfWeek.THURSDAY);
        workAlarm.addRepeatDay(DayOfWeek.FRIDAY);

        Alarm studyAlarm = new Alarm(LocalTime.of(17, 30), "Estudiar Programación", AlarmCategory.ESTUDIO);
        studyAlarm.setRequiresMathChallenge(true); // Funcionalidad avanzada activada

        Alarm medAlarm = new Alarm(LocalTime.of(21, 0), "Pastilla Tensión", AlarmCategory.MEDICINA);

        manager.addAlarm(workAlarm);
        manager.addAlarm(studyAlarm);
        manager.addAlarm(medAlarm);

        // ---------------------------------------------------------
        // 2. FILTRADO Y PRÓXIMAS ALARMAS
        // ---------------------------------------------------------
        System.out.println("\n--- FILTRANDO ALARMAS DE ESTUDIO ---");
        List<Alarm> studyAlarms = manager.getAlarmsByCategory(AlarmCategory.ESTUDIO);
        studyAlarms.forEach(System.out::println);

        LocalDateTime simulatedNow = LocalDateTime.now(); 
        System.out.println("\n--- PRÓXIMAS ALARMAS ACTIVAS (Desde " + simulatedNow.withNano(0) + ") ---");
        List<Alarm> upcoming = manager.getUpcomingAlarms(simulatedNow);
        for (Alarm a : upcoming) {
            System.out.println("- " + a.getLabel() + " sonará en: " + a.getNextTriggerTime(simulatedNow));
        }

        // ---------------------------------------------------------
        // 3. PRUEBA: MODO VACACIONES (Avanzado)
        // ---------------------------------------------------------
        System.out.println("\n--- PRUEBA: MODO VACACIONES ---");
        manager.setVacationMode(true);
        System.out.println("Total de alarmas próximas a sonar en vacaciones: " + manager.getUpcomingAlarms(simulatedNow).size());
        manager.setVacationMode(false); // Lo desactivamos para continuar la prueba
        System.out.println("Modo vacaciones desactivado. Restaurando alarmas...");

        // ---------------------------------------------------------
        // 4. PRUEBA: POSPONER Y DETENER (Básico)
        // ---------------------------------------------------------
        System.out.println("\n--- PRUEBA: ALARMA NORMAL (POSPONER Y DETENER) ---");
        workAlarm.trigger();
        workAlarm.snooze(simulatedNow);
        System.out.println("-> Nueva hora para '" + workAlarm.getLabel() + "': " + workAlarm.getNextTriggerTime(simulatedNow));
        workAlarm.stop(null); // Se detiene sin necesidad de reto matemático

        // ---------------------------------------------------------
        // 5. PRUEBA: RETO MATEMÁTICO (Avanzado)
        // ---------------------------------------------------------
        System.out.println("\n--- PRUEBA: ALARMA CON RETO MATEMÁTICO ---");
        studyAlarm.trigger(); // Comienza a sonar y genera el reto
        
        boolean isStopped = false;
        while (!isStopped) {
            System.out.print("Introduce la respuesta numérica para apagar la alarma: ");
            String input = scanner.nextLine();
            
            try {
                int answer = Integer.parseInt(input);
                // Si la respuesta es correcta, stop() devuelve true y sale del bucle
                isStopped = studyAlarm.stop(answer); 
            } catch (NumberFormatException e) {
                // Si el usuario introduce letras o lo deja en blanco
                System.out.println("⚠️ Por favor, introduce un número válido.");
                isStopped = studyAlarm.stop(null); 
            }
        }

        System.out.println("\n=== FIN DE LA SIMULACIÓN ===");
        scanner.close();
    }
}