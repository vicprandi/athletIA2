package athletia.controller;

import athletia.model.response.ExerciseResponse;
import athletia.service.ExerciseService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExerciseController {

    private final ExerciseService service;

    public ExerciseController(ExerciseService service) {
        this.service = service;
    }

    public List<ExerciseResponse> listAll() {
        return service.findAll();
    }
}
