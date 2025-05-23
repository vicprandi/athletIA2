package athletia.entrypoint;

import athletia.controller.WorkoutPlanController;
import athletia.model.request.WorkoutPlanRequest;
import athletia.model.response.WorkoutPlanResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout-plans")
@Tag(name = "Workout Plans")
public class WorkoutPlanEntrypoint {

    private final WorkoutPlanController controller;

    public WorkoutPlanEntrypoint(WorkoutPlanController controller) {
        this.controller = controller;
    }

    @Operation(summary = "Create a new workout plan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Workout plan created"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public WorkoutPlanResponse create(@Valid @RequestBody WorkoutPlanRequest request) {
        return controller.create(request);
    }

    @Operation(summary = "List all workout plans of authenticated user")
    @ApiResponse(responseCode = "200", description = "List of workout plans")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<WorkoutPlanResponse> list() {
        return controller.listByUser();
    }

    @Operation(summary = "Delete all workout plans of authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All workout plans deleted")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        controller.deleteAll();
    }

    @Operation(summary = "Delete a specific workout plan by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Workout plan deleted"),
            @ApiResponse(responseCode = "404", description = "Plan not found")
    })
    @DeleteMapping("/{planId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String planId) {
        controller.deleteById(planId);
    }

}