package athletia.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "exercises")
@Builder(toBuilder = true)
public record Exercise(
        @Id String id,
        String name,
        String muscleGroup,
        String type,
        String equipment,
        String description
) {}
