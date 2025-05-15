package athletia.entrypoint;

import athletia.controller.WorkoutPlanAIController;
import athletia.model.response.WorkoutPlanResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workout-plans/ai")
@Tag(name = "Workout Plans (AI)")
public class WorkoutPlanAIEntrypoint {

    private final WorkoutPlanAIController controller;

    public WorkoutPlanAIEntrypoint(WorkoutPlanAIController controller) {
        this.controller = controller;
    }

    @Operation(summary = "Generate a new workout plan using AI")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/generate")
    public WorkoutPlanResponse generateAIWorkout() {
        return controller.generate();
    }
}