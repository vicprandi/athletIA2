package athletia.repository;

import athletia.model.WorkoutExercise;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkoutExerciseRepository extends MongoRepository<WorkoutExercise, String> {

    List<WorkoutExercise> findAllByWorkoutPlanIdOrderByOrderAsc(String workoutPlanId);

}
