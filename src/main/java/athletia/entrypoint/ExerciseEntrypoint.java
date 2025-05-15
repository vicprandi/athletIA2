package athletia.entrypoint;

import athletia.controller.ExerciseController;
import athletia.model.response.ExerciseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exercises")
@Tag(name = "Exercises")
public class ExerciseEntrypoint {

    private final ExerciseController controller;

    public ExerciseEntrypoint(ExerciseController controller) {
        this.controller = controller;
    }

    @Operation(summary = "List all available exercises")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExerciseResponse> getAllExercises() {
        return controller.listAll();
    }
}
