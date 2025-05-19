package athletia.repository;

import athletia.model.WorkoutExercise;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutExerciseRepository extends MongoRepository<WorkoutExercise, String> {

    List<WorkoutExercise> findAllByWorkoutPlanIdOrderByOrderAsc(String workoutPlanId);

    List<WorkoutExercise> findByWorkoutPlanId(String workoutPlanId);

    void deleteAllByWorkoutPlanId(String workoutPlanId);

    Optional<WorkoutExercise> findByIdAndWorkoutPlanId(String id, String workoutPlanId);

}
