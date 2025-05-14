package athletia.controller;

import athletia.config.security.authentication.JwtService;
import athletia.entrypoint.WorkoutPlanEntrypoint;
import athletia.model.request.WorkoutPlanRequest;
import athletia.model.response.WorkoutPlanResponse;
import athletia.util.Goal;
import athletia.util.TrainingLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(WorkoutPlanEntrypoint.class)
@DisplayName("WorkoutPlanControllerTest – Testes de endpoint REST")
class WorkoutPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private WorkoutPlanController controller;

    private String validPlanJson;
    private WorkoutPlanResponse expectedResponse;

    @BeforeEach
    void setUp() {
        validPlanJson = """
        {
          "title": "Projeto Verão",
          "description": "Plano para perder gordura e ganhar definição",
          "durationWeeks": 6,
          "level": "BEGINNER",
          "goal": "WEIGHT_LOSS"
        }
        """;

        expectedResponse = new WorkoutPlanResponse(
                "123",
                "68250e00383ced220bc9bcc1",
                "Projeto Verão",
                "Plano para perder gordura e ganhar definição",
                6,
                TrainingLevel.BEGINNER,
                Goal.WEIGHT_LOSS,
                Instant.parse("2025-05-14T00:00:00Z")
        );

        when(controller.create(any(WorkoutPlanRequest.class)))
                .thenReturn(expectedResponse);

        when(controller.listByUser())
                .thenReturn(List.of(expectedResponse));
    }

    @Test
    @DisplayName("POST /workout-plans – Criar plano com dados válidos retorna 201 e payload correto")
    void whenPostCreateWorkoutPlan_withValidInput_thenReturns201AndResponseBody() throws Exception {
        mockMvc.perform(post("/workout-plans")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPlanJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.title").value("Projeto Verão"))
                .andExpect(jsonPath("$.goal").value("WEIGHT_LOSS"));
    }

    @Test
    @DisplayName("POST /workout-plans – Payload inválido retorna 400 Bad Request")
    void whenPostCreateWorkoutPlan_withInvalidInput_thenReturns400() throws Exception {
        var invalidJson = """
            {
              "title": "",
              "description": "",
              "durationWeeks": null,
              "level": null,
              "goal": null
            }
            """;

        mockMvc.perform(post("/workout-plans")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /workout-plans – Deve retornar lista de planos do usuário autenticado")
    void whenGetWorkoutPlans_thenReturns200AndList() throws Exception {
        mockMvc.perform(get("/workout-plans"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value("123"))
                .andExpect(jsonPath("$[0].title").value("Projeto Verão"))
                .andExpect(jsonPath("$[0].goal").value("WEIGHT_LOSS"));
    }
}
