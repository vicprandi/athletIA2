package athletia.model.response;

import athletia.util.Goal;
import athletia.util.TrainingLevel;

import java.time.Instant;
public record WorkoutPlanResponse(
        String id,
        String userId,
        String title,
        String description,
        Integer durationWeeks,
        TrainingLevel level,
        Goal goal,
        Instant createdAt
) {
}
