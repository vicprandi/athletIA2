package athletia.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Builder
@Document(collection = "users")
public record User(

        @Id
        String id,

        String name,

        String username,

        @Indexed(unique = true)
        String email,

        String password,

        Instant createdAt
) {}