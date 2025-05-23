package athletia.controller;

import athletia.model.request.WorkoutExerciseRequest;
import athletia.model.response.WorkoutExerciseResponse;
import athletia.model.response.WorkoutExerciseWithDetailsResponse;
import athletia.service.WorkoutExerciseService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkoutExerciseController {

    private final WorkoutExerciseService service;

    public WorkoutExerciseController(WorkoutExerciseService service) {
        this.service = service;
    }

    public WorkoutExerciseResponse add(String planId, WorkoutExerciseRequest request){
        return service.addExerciseToPlan(planId, request);
    }

    public List<WorkoutExerciseResponse> listByPlan(String planId){
        return service.getExercisesByPlan(planId);
    }

    public List<WorkoutExerciseWithDetailsResponse> listWithDetails(String planId) {
        return service.findWithDetailsByPlanId(planId);
    }

    public void delete(String planId, String exerciseId) {
        service.deleteByPlanIdAndExerciseId(planId, exerciseId);
    }

}
