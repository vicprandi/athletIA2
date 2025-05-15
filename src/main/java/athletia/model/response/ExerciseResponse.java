package athletia.model.response;

public record ExerciseResponse(
        String id,
        String name,
        String muscleGroup,
        String type,
        String equipment,
        String description
) {}
