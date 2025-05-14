package athletia.model.response;

import athletia.model.Gender;
import athletia.model.TrainingLevel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponse(

        String id,

        String name,

        String username,

        String email,

        Double height,

        Double weight,

        LocalDate birthDate,

        Gender gender,

        TrainingLevel level,

        Instant createdAt

) {}

