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
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkoutExerciseServiceImpl implements WorkoutExerciseService {

    private final WorkoutExerciseRepository repository;
    private final ExerciseRepository exerciseRepository;
    private final GenericMapper mapper;

    public WorkoutExerciseServiceImpl(
            WorkoutExerciseRepository repository,
            ExerciseRepository exerciseRepository,
            GenericMapper mapper
    ) {
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
        List<WorkoutExercise> workouts = repository.findByWorkoutPlanId(planId);

        Map<String, Exercise> catalog = exerciseRepository.findAll().stream()
                .collect(Collectors.toMap(Exercise::id, e -> e));

        return workouts.stream()
                .map(e -> {
                    Exercise exercise = catalog.getOrDefault(
                            e.exerciseId(),
                            new Exercise(
                                    "(desconhecido)",      // id
                                    "(sem nome)",          // name
                                    "(sem grupo)",         // muscleGroup
                                    "(sem tipo)",          // type
                                    "(sem equipamento)",   // equipment
                                    "(sem descrição)"      // description
                            )
                    );

                    return new WorkoutExerciseWithDetailsResponse(
                            e.id(),
                            e.workoutPlanId(),
                            e.order(),
                            e.sets(),
                            e.reps(),
                            e.restSeconds(),
                            e.suggestedLoad(),
                            exercise
                    );
                })
                .toList();
    }

    @Override
    public void deleteByPlanIdAndExerciseId(String planId, String exerciseId) {
        WorkoutExercise existing = repository.findByIdAndWorkoutPlanId(exerciseId, planId)
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado no plano"));

        repository.delete(existing);
    }
}