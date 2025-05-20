package athletia.repository;

import athletia.model.WorkoutPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WorkoutPlanRepository extends MongoRepository<WorkoutPlan, String> {

    List<WorkoutPlan> findAllByUserId(String userId);

    void deleteById(String id);

}