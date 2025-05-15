package athletia.model.response;

public record WorkoutExerciseResponse(
        String id,
        String workoutPlanId,
        String exerciseId,
        Integer order,
        Integer sets,
        Integer reps,
        Integer restSeconds,
        Double suggestedLoad
) {}
