package athletia.model;

import athletia.util.Gender;
import athletia.util.Goal;
import athletia.util.TrainingLevel;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Builder(toBuilder = true)
@Document(collection = "users")
public record User(

        @Id
        String id,

        String name,

        String username,

        @Indexed(unique = true)
        String email,

        String password,

        Double height,

        Double weight,

        LocalDate birthDate,

        Gender gender,

        TrainingLevel level,

        Goal goal,

        Instant createdAt
) {}