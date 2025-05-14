package athletia.model.response;


import java.time.Instant;

public record ErrorResponse(
        String path,
        String error,
        String message,
        Instant timestamp,
        int status
) {}
