package athletia.model.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record WorkoutSessionRequest(
        @NotNull LocalDate date,
        @NotNull Integer durationMinutes,
        String performanceNotes,
        @Min(1) @Max(10) Integer rpe
) {}