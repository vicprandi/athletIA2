package athletia.model;

import athletia.util.Goal;
import athletia.util.TrainingLevel;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Builder(toBuilder = true)
@Document(collection = "workout_plans")
public record WorkoutPlan(
        @Id
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
