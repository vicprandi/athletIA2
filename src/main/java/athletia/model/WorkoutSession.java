package athletia.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Builder(toBuilder = true)
@Document(collection = "workout_sessions")
public record WorkoutSession(
        @Id String id,
        String userId,
        String workoutPlanId,
        LocalDate date,
        Integer durationMinutes,
        Integer rpe, // Rate of Perceived Exertion (1 a 10)
        String performanceNotes,
        Instant createdAt
) {}