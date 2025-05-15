package athletia.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder()
@Document(collection = "workout_exercise")
public record WorkoutExercise (

        @Id
        String id,
        String workoutPlanId,
        String exerciseId,
        Integer order,
        Integer sets,
        Integer reps,
        Integer restSeconds,
        Double suggestedLoad
){
}
