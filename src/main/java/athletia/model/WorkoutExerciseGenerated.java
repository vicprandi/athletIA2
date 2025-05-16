package athletia.model;

public record WorkoutExerciseGenerated(
        String exerciseId,
        String name,
        String muscleGroup,
        int sets,
        int reps,
        int restSeconds,
        double suggestedLoad
) {}