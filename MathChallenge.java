import java.util.Random;

public class MathChallenge {
    private final int operandA;
    private final int operandB;
    private final int correctAnswer;
    private final String question;

    public MathChallenge() {
        Random rand = new Random();
        this.operandA = rand.nextInt(20) + 1; // 1 a 20
        this.operandB = rand.nextInt(20) + 1; // 1 a 20
        this.correctAnswer = operandA + operandB;
        this.question = "¿Cuánto es " + operandA + " + " + operandB + "?";
    }

    public String getQuestion() { return question; }
    
    public boolean verify(int attempt) {
        return attempt == correctAnswer;
    }
}