package athletia.model.response;

import athletia.model.Exercise;

public record WorkoutExerciseWithDetailsResponse(
        String id,
        String workoutPlanId,
        Integer order,
        Integer sets,
        Integer reps,
        Integer restSeconds,
        Double suggestedLoad,
        Exercise exercise
) {}
