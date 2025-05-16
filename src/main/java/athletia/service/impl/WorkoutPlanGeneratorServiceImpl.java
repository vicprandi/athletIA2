package athletia.service.impl;

import athletia.model.*;
import athletia.model.response.WorkoutPlanResponse;
import athletia.repository.UserRepository;
import athletia.repository.WorkoutExerciseRepository;
import athletia.repository.WorkoutPlanRepository;
import athletia.service.CurrentUserService;
import athletia.service.UserService;
import athletia.service.WorkoutPlanAIService;
import athletia.service.WorkoutPlanGeneratorService;
import athletia.config.mapper.GenericMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import athletia.model.*;
import athletia.model.response.WorkoutPlanResponse;
import athletia.repository.UserRepository;
import athletia.repository.WorkoutExerciseRepository;
import athletia.repository.WorkoutPlanRepository;
import athletia.service.CurrentUserService;
import athletia.service.UserService;
import athletia.service.WorkoutPlanAIService;
import athletia.service.WorkoutPlanGeneratorService;
import athletia.config.mapper.GenericMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class WorkoutPlanGeneratorServiceImpl implements WorkoutPlanGeneratorService {

    private final CurrentUserService currentUserService;
    private final UserService userService;
    private final WorkoutPlanAIService workoutPlanAIService;
    private final WorkoutPlanRepository planRepository;
    private final UserRepository repository;
    private final WorkoutExerciseRepository exerciseRepository;
    private final GenericMapper mapper;

    public WorkoutPlanGeneratorServiceImpl(
            CurrentUserService currentUserService,
            UserService userService,
            WorkoutPlanAIService workoutPlanAIService,
            WorkoutPlanRepository planRepository, UserRepository repository,
            WorkoutExerciseRepository exerciseRepository,
            GenericMapper mapper
    ) {
        this.currentUserService = currentUserService;
        this.userService = userService;
        this.workoutPlanAIService = workoutPlanAIService;
        this.planRepository = planRepository;
        this.repository = repository;
        this.exerciseRepository = exerciseRepository;
        this.mapper = mapper;
    }

    @Override
    public WorkoutPlanResponse generateForAuthenticatedUser() {
        String userId = currentUserService.getCurrentUserId();
        User user = userService.getFullUserEntityById(userId);

        WorkoutPlanGenerated generated = workoutPlanAIService.generateWorkoutPlan(user);

        WorkoutPlan plan = WorkoutPlan.builder()
                .userId(userId)
                .title(generated.title())
                .description(generated.description())
                .durationWeeks(generated.durationWeeks())
                .level(user.level())
                .goal(user.goal())
                .createdAt(Instant.now())
                .build();

        WorkoutPlan savedPlan = planRepository.save(plan);

        List<WorkoutExercise> exercises = IntStream.range(0, generated.exercises().size())
                .mapToObj(i -> {
                    WorkoutExerciseGenerated g = generated.exercises().get(i);
                    return WorkoutExercise.builder()
                            .workoutPlanId(savedPlan.id())
                            .exerciseId(g.exerciseId())
                            .order(i + 1)
                            .sets(g.sets())
                            .reps(g.reps())
                            .restSeconds(g.restSeconds())
                            .suggestedLoad(g.suggestedLoad())
                            .build();
                })
                .toList();

        exerciseRepository.saveAll(exercises);

        return mapper.map(savedPlan, WorkoutPlanResponse.class);
    }
}
