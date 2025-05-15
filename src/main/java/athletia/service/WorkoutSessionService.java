package athletia.service;

import athletia.model.request.WorkoutSessionRequest;
import athletia.model.response.WorkoutSessionResponse;

import java.util.List;

public interface WorkoutSessionService {

    WorkoutSessionResponse register(String workoutPlanId, WorkoutSessionRequest request);

    List<WorkoutSessionResponse> findAllByAuthenticatedUser();

    void deleteById(String sessionId);

}