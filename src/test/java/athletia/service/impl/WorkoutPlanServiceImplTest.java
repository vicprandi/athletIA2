package athletia.service.impl;

import athletia.config.mapper.GenericMapper;
import athletia.model.WorkoutPlan;
import athletia.model.request.WorkoutPlanRequest;
import athletia.model.response.WorkoutPlanResponse;
import athletia.repository.WorkoutPlanRepository;
import athletia.service.CurrentUserService;
import athletia.util.Goal;
import athletia.util.TrainingLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("WorkoutPlanServiceImplTest – Testes unitários do service")
class WorkoutPlanServiceImplTest {

    @Mock
    private WorkoutPlanRepository repository;

    @Mock
    private GenericMapper mapper;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private WorkoutPlanServiceImpl service;

    private WorkoutPlanRequest validRequest;
    private WorkoutPlan workoutPlanEntity;
    private WorkoutPlanResponse workoutPlanResponse;

    @BeforeEach
    void setUp() {
        validRequest = new WorkoutPlanRequest(
                "Projeto Verão",
                "Plano para perder gordura e ganhar definição",
                6,
                TrainingLevel.BEGINNER,
                Goal.WEIGHT_LOSS
        );

        workoutPlanEntity = WorkoutPlan.builder()
                .id("123")
                .userId("user-001")
                .title("Projeto Verão")
                .description("Plano para perder gordura e ganhar definição")
                .durationWeeks(6)
                .level(TrainingLevel.BEGINNER)
                .goal(Goal.WEIGHT_LOSS)
                .createdAt(Instant.parse("2025-05-14T00:00:00Z"))
                .build();

        workoutPlanResponse = new WorkoutPlanResponse(
                "123",
                "user-001",
                "Projeto Verão",
                "Plano para perder gordura e ganhar definição",
                6,
                TrainingLevel.BEGINNER,
                Goal.WEIGHT_LOSS,
                Instant.parse("2025-05-14T00:00:00Z")
        );

        when(currentUserService.getCurrentUserId())
                .thenReturn("user-001");

        when(repository.save(any(WorkoutPlan.class)))
                .thenReturn(workoutPlanEntity);

        when(mapper.map(workoutPlanEntity, WorkoutPlanResponse.class))
                .thenReturn(workoutPlanResponse);
    }

    @Test
    @DisplayName("createWorkoutPlan – Quando dados válidos, retorna WorkoutPlanResponse e salva no banco")
    void createWorkoutPlan_withValidRequest_shouldReturnResponse() {
        WorkoutPlanResponse result = service.createWorkoutPlan(validRequest);

        assertSame(workoutPlanResponse, result);

        verify(currentUserService).getCurrentUserId();
        verify(repository).save(any(WorkoutPlan.class));
        verify(mapper).map(workoutPlanEntity, WorkoutPlanResponse.class);
    }

    @Test
    @DisplayName("getAllWorkoutPlansByUser – Deve retornar lista de planos do usuário autenticado")
    void getAllWorkoutPlansByUser_shouldReturnList() {
        when(repository.findAllByUserId("user-001"))
                .thenReturn(List.of(workoutPlanEntity));

        List<WorkoutPlanResponse> result = service.getAllWorkoutPlansByUser();

        assertEquals(1, result.size());
        assertSame(workoutPlanResponse, result.get(0));

        verify(currentUserService).getCurrentUserId();
        verify(repository).findAllByUserId("user-001");
        verify(mapper).map(workoutPlanEntity, WorkoutPlanResponse.class);
    }
}
