package athletia.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExerciseResponse(
        @JsonProperty("_id") String id,
        String name,
        String muscleGroup,
        String type,
        String equipment,
        String description
) {}
