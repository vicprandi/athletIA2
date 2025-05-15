package athletia.service;

import athletia.model.request.WorkoutExerciseRequest;
import athletia.model.response.WorkoutExerciseResponse;

import java.util.List;

public interface WorkoutExerciseService {

    WorkoutExerciseResponse addExerciseToPlan(String planId, WorkoutExerciseRequest request);

    List<WorkoutExerciseResponse> getExercisesByPlan(String planId);
}
