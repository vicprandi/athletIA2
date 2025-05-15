package athletia.model.request;

public record ExerciseRequest(
        String name,
        String muscleGroup,
        String type,
        String equipment,
        String description
) {}

