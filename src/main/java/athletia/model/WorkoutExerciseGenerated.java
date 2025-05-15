package athletia.model;

public record WorkoutExerciseGenerated(
        String name,
        String muscleGroup,
        int sets,
        int reps,
        int restSeconds,
        double suggestedLoad
) {}