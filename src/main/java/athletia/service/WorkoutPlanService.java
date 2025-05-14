package athletia.service;

import athletia.model.request.WorkoutPlanRequest;
import athletia.model.response.WorkoutPlanResponse;

import java.util.List;

public interface WorkoutPlanService {

    WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request);

    List<WorkoutPlanResponse> getAllWorkoutPlansByUser();
}