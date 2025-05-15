package athletia.service;

import athletia.model.response.WorkoutPlanResponse;

public interface WorkoutPlanGeneratorService {
    WorkoutPlanResponse generateForAuthenticatedUser();
}