package athletia.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "exercises")
@Builder(toBuilder = true)
public record Exercise(
        @Id @JsonProperty("_id") String id,
        String name,
        String muscleGroup,
        String type,
        String equipment,
        String description
) {}
