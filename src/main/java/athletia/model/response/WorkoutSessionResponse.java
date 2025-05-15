package athletia.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WorkoutSessionResponse(
        String id,
        String workoutPlanId,
        LocalDate date,
        Integer durationMinutes,
        Integer rpe,
        String performanceNotes,
        Instant createdAt
) {}