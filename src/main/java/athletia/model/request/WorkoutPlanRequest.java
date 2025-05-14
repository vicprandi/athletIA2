package athletia.model.request;

import athletia.util.Goal;
import athletia.util.TrainingLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WorkoutPlanRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Duration is required")
        Integer durationWeeks,

        @NotNull(message = "Level is required")
        TrainingLevel level,

        @NotNull(message = "Goal is required")
        Goal goal
) {
}
