package athletia.model.request;

import athletia.util.Gender;
import athletia.util.Goal;
import athletia.util.TrainingLevel;

import java.time.LocalDate;

public record UserProfileUpdateRequest(
        Double height,
        Double weight,
        LocalDate birthDate,
        Gender gender,
        TrainingLevel level,
        Goal goal
) {}