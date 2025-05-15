package athletia.service;

import athletia.model.request.WorkoutExerciseRequest;
import athletia.model.response.WorkoutExerciseResponse;
import athletia.model.response.WorkoutExerciseWithDetailsResponse;

import java.util.List;

public interface WorkoutExerciseService {

    WorkoutExerciseResponse addExerciseToPlan(String planId, WorkoutExerciseRequest request);

    List<WorkoutExerciseResponse> getExercisesByPlan(String planId);

    List<WorkoutExerciseWithDetailsResponse> findWithDetailsByPlanId(String planId);

}
