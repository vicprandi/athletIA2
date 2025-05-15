package athletia.entrypoint;

import athletia.controller.WorkoutSessionController;
import athletia.model.request.WorkoutSessionRequest;
import athletia.model.response.WorkoutSessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout-sessions")
@Tag(name = "Execution Sessions")
public class WorkoutSessionEntrypoint {

    private final WorkoutSessionController controller;

    public WorkoutSessionEntrypoint(WorkoutSessionController controller) {
        this.controller = controller;
    }

    @Operation(summary = "Register new Workout Session")
    @PostMapping("/{planId}")
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutSessionResponse register(
            @PathVariable String planId,
            @Valid @RequestBody WorkoutSessionRequest request
    ) {
        return controller.register(planId, request);
    }

    @Operation(summary = "List all workout sessions")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WorkoutSessionResponse> list() {
        return controller.listAllFromCurrentUser();
    }

    @Operation(summary = "Delete a session")
    @DeleteMapping("/{sessionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String sessionId) {
        controller.deleteById(sessionId);
    }

}