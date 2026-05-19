import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Alarm {
    private final String id;
    private LocalTime time;
    private String label;
    private boolean isActive;
    private Set<DayOfWeek> repeatDays;
    private String sound;
    private int volume;

    // Atributos avanzados
    private AlarmCategory category;
    private boolean requiresMathChallenge;
    private MathChallenge currentChallenge;

    // Estado interno para posponer (snooze)
    private LocalDateTime snoozedUntil;
    private static final int SNOOZE_MINUTES = 5;

    public Alarm(LocalTime time, String label, AlarmCategory category) {
        this.id = UUID.randomUUID().toString();
        this.time = time;
        this.label = label;
        this.isActive = true;
        this.repeatDays = new HashSet<>();
        this.sound = "default.mp3";
        this.volume = 50;
        this.snoozedUntil = null;
        
        this.category = category;
        this.requiresMathChallenge = false;
        this.currentChallenge = null;
    }

    // --- GETTERS & SETTERS ---
    public String getId() { return id; }
    public String getLabel() { return label; }
    public boolean isActive() { return isActive; }
    public AlarmCategory getCategory() { return category; }
    public String getSound() { return sound; } 
    public int getVolume() { return volume; }  
    
    public void setTime(LocalTime time) { this.time = time; }
    public void setLabel(String label) { this.label = label; }
    public void setActive(boolean active) { this.isActive = active; }
    public void setSound(String sound) { this.sound = sound; }
    public void setVolume(int volume) { this.volume = Math.max(0, Math.min(100, volume)); }
    public void setRequiresMathChallenge(boolean requires) { this.requiresMathChallenge = requires; }
    
    public void addRepeatDay(DayOfWeek day) { this.repeatDays.add(day); }
    public void removeRepeatDay(DayOfWeek day) { this.repeatDays.remove(day); }
    public void clearRepeatDays() { this.repeatDays.clear(); }

    // --- LÓGICA DE NEGOCIO ---

    public LocalDateTime getNextTriggerTime(LocalDateTime from) {
        if (!isActive) return null;
        if (snoozedUntil != null) return snoozedUntil; 

        LocalDateTime nextTime = from.with(time).withSecond(0).withNano(0);

        if (repeatDays.isEmpty()) {
            if (nextTime.isBefore(from) || nextTime.isEqual(from)) {
                nextTime = nextTime.plusDays(1);
            }
            return nextTime;
        } else {
            if (repeatDays.contains(from.getDayOfWeek()) && nextTime.isAfter(from)) {
                return nextTime; 
            }
            for (int i = 1; i <= 7; i++) {
                LocalDateTime candidate = nextTime.plusDays(i);
                if (repeatDays.contains(candidate.getDayOfWeek())) {
                    return candidate;
                }
            }
        }
        return null;
    }

    public void snooze(LocalDateTime now) {
        if (isActive) {
            this.snoozedUntil = now.plusMinutes(SNOOZE_MINUTES);
            System.out.println("⏳ Alarma '" + label + "' pospuesta " + SNOOZE_MINUTES + " minutos.");
        }
    }

    public void trigger() {
        System.out.println("🔔 ¡RING! La alarma '" + label + "' está sonando.");
        if (requiresMathChallenge) {
            this.currentChallenge = new MathChallenge();
            System.out.println("   -> RETO ACTIVO: " + currentChallenge.getQuestion());
        }
    }

    public boolean stop(Integer mathAnswer) {
        if (requiresMathChallenge && currentChallenge != null) {
            if (mathAnswer == null || !currentChallenge.verify(mathAnswer)) {
                System.out.println("❌ Respuesta incorrecta o vacía. ¡La alarma sigue sonando!");
                return false; 
            }
            System.out.println("✅ ¡Respuesta correcta!");
            this.currentChallenge = null; 
        }

        this.snoozedUntil = null;
        if (repeatDays.isEmpty()) {
            this.isActive = false;
        }
        System.out.println("⏹️ Alarma '" + label + "' detenida exitosamente.");
        return true;
    }

    // --- FORMATEO DE TEXTO ---
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s (Cat: %s, Reto: %b, Sonido: %s, Vol: %d%%)", 
                (isActive ? "ACTIVA" : "INACTIVA"), time.toString(), label, category, requiresMathChallenge, sound, volume);
    }
}