package athletia.repository;

import athletia.model.Exercise;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExerciseRepository extends MongoRepository<Exercise, String> {
}
