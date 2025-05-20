package athletia.service.impl;

import athletia.config.mapper.GenericMapper;
import athletia.model.WorkoutPlan;
import athletia.model.request.WorkoutPlanRequest;
import athletia.model.response.WorkoutPlanResponse;
import athletia.repository.WorkoutExerciseRepository;
import athletia.repository.WorkoutPlanRepository;
import athletia.repository.WorkoutSessionRepository;
import athletia.service.CurrentUserService;
import athletia.service.WorkoutPlanService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

    private final WorkoutPlanRepository repository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final GenericMapper mapper;
    private final CurrentUserService currentUserService;

    public WorkoutPlanServiceImpl(WorkoutPlanRepository repository, WorkoutExerciseRepository workoutExerciseRepository, WorkoutSessionRepository workoutSessionRepository, GenericMapper mapper, CurrentUserService currentUserService) {
        this.repository = repository;
        this.workoutExerciseRepository = workoutExerciseRepository;
        this.workoutSessionRepository = workoutSessionRepository;
        this.mapper = mapper;
        this.currentUserService = currentUserService;
    }

    @Override
    public WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request) {
        String userId = currentUserService.getCurrentUserId();

        WorkoutPlan plan = WorkoutPlan.builder()
                .userId(userId)
                .title(request.title())
                .description(request.description())
                .durationWeeks(request.durationWeeks())
                .level(request.level())
                .goal(request.goal())
                .createdAt(Instant.now())
                .build();

        return mapper.map(repository.save(plan), WorkoutPlanResponse.class);
    }

    @Override
    public List<WorkoutPlanResponse> getAllWorkoutPlansByUser() {
        String userId = currentUserService.getCurrentUserId();

        return repository.findAllByUserId(userId).stream()
                .map(plan -> mapper.map(plan, WorkoutPlanResponse.class))
                .toList();
    }

    @Override
    public void deleteAllWorkoutPlansForAuthenticatedUser() {
        String userId = currentUserService.getCurrentUserId();

        List<WorkoutPlan> plans = repository.findAllByUserId(userId);

        for (WorkoutPlan plan : plans) {
            workoutExerciseRepository.deleteAllByWorkoutPlanId(plan.id());
            workoutSessionRepository.deleteAllByWorkoutPlanId(plan.id());

            repository.deleteById(plan.id());
        }
    }

    @Override
    public void deleteById(String planId) {
        WorkoutPlan plan = repository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado"));

        String currentUserId = currentUserService.getCurrentUserId();
        if (!plan.userId().equals(currentUserId)) {
            throw new RuntimeException("Acesso negado ao plano de outro usuário");
        }

        workoutExerciseRepository.deleteAllByWorkoutPlanId(plan.id());
        workoutSessionRepository.deleteAllByWorkoutPlanId(plan.id());

        repository.deleteById(planId);
    }

}