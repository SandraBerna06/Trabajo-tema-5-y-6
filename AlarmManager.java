import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlarmManager {
    private final List<Alarm> alarms;
    private boolean vacationMode; // Atributo para el modo vacaciones

    public AlarmManager() {
        this.alarms = new ArrayList<>();
        this.vacationMode = false;
    }

    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
        System.out.println("✅ Alarma creada: " + alarm.getLabel());
    }

    public boolean removeAlarm(String id) {
        boolean removed = alarms.removeIf(alarm -> alarm.getId().equals(id));
        if (removed) System.out.println("🗑️ Alarma eliminada.");
        return removed;
    }

    public void toggleAlarm(String id, boolean active) {
        for (Alarm alarm : alarms) {
            if (alarm.getId().equals(id)) {
                alarm.setActive(active);
                System.out.println("🔄 Alarma '" + alarm.getLabel() + "' " + (active ? "activada" : "desactivada") + ".");
                return;
            }
        }
    }

    // --- FUNCIONES AVANZADAS ---

    public void setVacationMode(boolean vacationMode) {
        this.vacationMode = vacationMode;
        System.out.println("🌴 Modo vacaciones " + (vacationMode ? "ACTIVADO. No sonará ninguna alarma." : "DESACTIVADO."));
    }

    public List<Alarm> getAlarmsByCategory(AlarmCategory category) {
        return alarms.stream()
            .filter(a -> a.getCategory() == category)
            .collect(Collectors.toList());
    }

    public List<Alarm> getUpcomingAlarms(LocalDateTime now) {
        // Si el modo vacaciones está activo, bloqueamos la salida de próximas alarmas
        if (vacationMode) {
            return new ArrayList<>(); 
        }

        return alarms.stream()
            .filter(Alarm::isActive)
            .sorted((a1, a2) -> {
                LocalDateTime t1 = a1.getNextTriggerTime(now);
                LocalDateTime t2 = a2.getNextTriggerTime(now);
                if (t1 == null) return 1;
                if (t2 == null) return -1;
                return t1.compareTo(t2);
            })
            .collect(Collectors.toList());
    }

    public void printAllAlarms() {
        System.out.println("\n--- LISTA DE ALARMAS ---");
        if (alarms.isEmpty()) {
            System.out.println("No hay alarmas configuradas.");
        } else {
            alarms.forEach(System.out::println);
        }
        System.out.println("------------------------\n");
    }
}