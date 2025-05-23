package athletia.entrypoint;

import athletia.controller.WorkoutExerciseController;
import athletia.model.request.WorkoutExerciseRequest;
import athletia.model.response.WorkoutExerciseResponse;
import athletia.model.response.WorkoutExerciseWithDetailsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout-plans/{planId}/exercises")
@Tag(name = "Workout Exercises")
public class WorkoutExerciseEntrypoint {

    private final WorkoutExerciseController controller;

    public WorkoutExerciseEntrypoint(WorkoutExerciseController controller) {
        this.controller = controller;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkoutExerciseResponse addExercise(
            @PathVariable String planId,
            @RequestBody @Valid WorkoutExerciseRequest request) {
        return controller.add(planId, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WorkoutExerciseResponse> list(@PathVariable String planId) {
        return controller.listByPlan(planId);
    }

    @Operation(summary = "List all exercises of a workout plan with full exercise details")
    @GetMapping("/full")
    @ResponseStatus(HttpStatus.OK)
    public List<WorkoutExerciseWithDetailsResponse> getWithDetails(@PathVariable String planId) {
        return controller.listWithDetails(planId);
    }

    @DeleteMapping("/{workoutExerciseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExercise(
            @PathVariable String planId,
            @PathVariable String workoutExerciseId) {
        controller.delete(planId, workoutExerciseId);
    }

}