package athletia.service.impl;

import athletia.config.mapper.GenericMapper;
import athletia.model.Exercise;
import athletia.model.WorkoutExercise;
import athletia.model.request.WorkoutExerciseRequest;
import athletia.model.response.WorkoutExerciseResponse;
import athletia.model.response.WorkoutExerciseWithDetailsResponse;
import athletia.repository.ExerciseRepository;
import athletia.repository.WorkoutExerciseRepository;
import athletia.service.WorkoutExerciseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final WorkoutExerciseRepository repository;
    private final ExerciseRepository exerciseRepository;
    private final GenericMapper mapper;

    public WorkoutExerciseServiceImpl(WorkoutExerciseRepository repository, ExerciseRepository exerciseRepository, GenericMapper mapper) {
        this.repository = repository;
        this.exerciseRepository = exerciseRepository;
        this.mapper = mapper;
    }

    @Override
    public WorkoutExerciseResponse addExerciseToPlan(String planId, WorkoutExerciseRequest request) {
        exerciseRepository.findById(request.exerciseId())
                .orElseThrow(() -> new RuntimeException("Invalid exerciseId: " + request.exerciseId()));

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

    @Override
    public List<WorkoutExerciseWithDetailsResponse> findWithDetailsByPlanId(String planId) {
        List<WorkoutExercise> workoutExercises = repository.findAllByWorkoutPlanIdOrderByOrderAsc(planId);

        return workoutExercises.stream().map(we -> {
            Exercise exercise = exerciseRepository.findById(we.exerciseId())
                    .orElseThrow(() -> new RuntimeException("Exercise not found for ID: " + we.exerciseId()));

            return new WorkoutExerciseWithDetailsResponse(
                    we.id(),
                    we.workoutPlanId(),
                    we.order(),
                    we.sets(),
                    we.reps(),
                    we.restSeconds(),
                    we.suggestedLoad(),
                    exercise
            );
        }).toList();
    }
}
