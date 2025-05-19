package athletia.config.handler;

import athletia.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntime(RuntimeException ex, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleJsonParseError(HttpMessageNotReadableException ex, WebRequest request) {
        return new ErrorResponse(
                request.getDescription(false).replace("uri=", ""),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Invalid or malformed request body",
                Instant.now(),
                HttpStatus.BAD_REQUEST.value()
        );
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();

        switch (ex.getMessage()) {
            case "Email taken." -> response.put("error", "EMAIL_ALREADY_EXISTS");
            case "Username taken." -> response.put("error", "USERNAME_ALREADY_EXISTS");
            case "Password must have uppercase, lowercase and special characters." -> response.put("error", "INVALID_PASSWORD");
            default -> {
                response.put("error", "UNKNOWN_ERROR");
                response.put("message", ex.getMessage());
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .header("Content-Type", "application/json")
                .body(response);
    }


    private ResponseEntity<Object> buildResponse(HttpStatus status, String message, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, status);
    }
}
