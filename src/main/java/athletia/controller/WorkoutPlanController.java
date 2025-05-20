package athletia.controller;

import athletia.model.request.WorkoutPlanRequest;
import athletia.model.response.WorkoutPlanResponse;
import athletia.service.WorkoutPlanService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkoutPlanController {

    private final WorkoutPlanService service;

    public WorkoutPlanController(WorkoutPlanService service) {
        this.service = service;
    }

    public WorkoutPlanResponse create(WorkoutPlanRequest request) {
        return service.createWorkoutPlan(request);
    }

    public List<WorkoutPlanResponse> listByUser() {
        return service.getAllWorkoutPlansByUser();
    }

    public void deleteAll() {
        service.deleteAllWorkoutPlansForAuthenticatedUser();
    }

    public void deleteById(String planId) {
        service.deleteById(planId);
    }

}