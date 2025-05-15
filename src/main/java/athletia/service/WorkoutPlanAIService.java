package athletia.service;

import athletia.model.User;
import athletia.model.WorkoutPlanGenerated;

public interface WorkoutPlanAIService {
    WorkoutPlanGenerated generateWorkoutPlan(User user);
}