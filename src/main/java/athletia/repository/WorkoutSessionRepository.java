package athletia.repository;

import athletia.model.WorkoutSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkoutSessionRepository extends MongoRepository<WorkoutSession, String> {

    List<WorkoutSession> findAllByUserIdOrderByDateDesc(String userId);

    void deleteAllByWorkoutPlanId(String workoutPlanId);

}