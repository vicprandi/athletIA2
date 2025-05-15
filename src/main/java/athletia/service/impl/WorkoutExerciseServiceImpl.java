package athletia.service.impl;

import athletia.config.mapper.GenericMapper;
import athletia.model.WorkoutExercise;
import athletia.model.request.WorkoutExerciseRequest;
import athletia.model.response.WorkoutExerciseResponse;
import athletia.repository.WorkoutExerciseRepository;
import athletia.service.WorkoutExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final WorkoutExerciseRepository repository;
    private final GenericMapper mapper;

    public WorkoutExerciseServiceImpl(WorkoutExerciseRepository repository, GenericMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public WorkoutExerciseResponse addExerciseToPlan(String planId, WorkoutExerciseRequest request) {
        WorkoutExercise exercise = WorkoutExercise.builder()
                .workoutPlanId(planId)
                .exerciseId(request.exerciseId())
                .order(request.order())
                .sets(request.sets())
                .reps(request.reps())
                .restSeconds(request.restSeconds())
                .suggestedLoad(request.suggestedLoad())
                .build();

        return mapper.map(repository.save(exercise), WorkoutExerciseResponse.class);
    }

    @Override
    public List<WorkoutExerciseResponse> getExercisesByPlan(String planId) {
        return repository.findAllByWorkoutPlanIdOrderByOrderAsc(planId).stream()
                .map(e -> mapper.map(e, WorkoutExerciseResponse.class))
                .toList();
    }
}
