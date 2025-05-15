package athletia.service.impl;

import athletia.config.mapper.GenericMapper;
import athletia.model.WorkoutSession;
import athletia.model.request.WorkoutSessionRequest;
import athletia.model.response.WorkoutSessionResponse;
import athletia.repository.WorkoutSessionRepository;
import athletia.service.CurrentUserService;
import athletia.service.WorkoutSessionService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class WorkoutSessionServiceImpl implements WorkoutSessionService {

    private final WorkoutSessionRepository repository;
    private final GenericMapper mapper;
    private final CurrentUserService currentUserService;

    public WorkoutSessionServiceImpl(
            WorkoutSessionRepository repository,
            GenericMapper mapper,
            CurrentUserService currentUserService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.currentUserService = currentUserService;
    }

    @Override
    public WorkoutSessionResponse register(String workoutPlanId, WorkoutSessionRequest request) {
        WorkoutSession session = WorkoutSession.builder()
                .userId(currentUserService.getCurrentUserId())
                .workoutPlanId(workoutPlanId)
                .date(request.date())
                .durationMinutes(request.durationMinutes())
                .rpe(request.rpe())
                .performanceNotes(request.performanceNotes())
                .createdAt(Instant.now())
                .build();

        return mapper.map(repository.save(session), WorkoutSessionResponse.class);
    }

    @Override
    public List<WorkoutSessionResponse> findAllByAuthenticatedUser() {
        String userId = currentUserService.getCurrentUserId();

        return repository.findAllByUserIdOrderByDateDesc(userId).stream()
                .map(ws -> mapper.map(ws, WorkoutSessionResponse.class))
                .toList();
    }

    @Override
    public void deleteById(String sessionId) {
        String userId = currentUserService.getCurrentUserId();

        WorkoutSession session = repository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.userId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this session");
        }

        repository.deleteById(sessionId);
    }
}