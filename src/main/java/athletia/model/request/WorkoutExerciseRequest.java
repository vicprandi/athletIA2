package athletia.model.request;

public record WorkoutExerciseRequest(
        Integer order,
        String exerciseId,
        Integer sets,
        Integer reps,
        Integer restSeconds,
        Double suggestedLoad
) {}
