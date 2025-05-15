package athletia.controller;

import athletia.model.response.WorkoutPlanResponse;
import athletia.service.WorkoutPlanGeneratorService;
import org.springframework.stereotype.Component;

@Component
public class WorkoutPlanAIController {

    private final WorkoutPlanGeneratorService generatorService;

    public WorkoutPlanAIController(WorkoutPlanGeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    public WorkoutPlanResponse generate() {
        return generatorService.generateForAuthenticatedUser();
    }
}