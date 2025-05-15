package athletia.entrypoint;

import athletia.controller.WorkoutExerciseController;
import athletia.model.request.WorkoutExerciseRequest;
import athletia.model.response.WorkoutExerciseResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout-plans/{planId}/exercises")
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
}
