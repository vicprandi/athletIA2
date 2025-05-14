package athletia.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponse(

        String id,

        String name,

        String username,

        String email,

        Instant createdAt) {}

