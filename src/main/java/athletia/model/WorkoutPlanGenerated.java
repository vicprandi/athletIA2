package athletia.model;

import java.util.List;

public record WorkoutPlanGenerated(
        String title,
        String description,
        int durationWeeks,
        List<WorkoutExerciseGenerated> exercises
) {}