package athletia.controller;

import athletia.config.security.authentication.JwtService;
import athletia.entrypoint.UserEntrypoint;
import athletia.model.request.UserProfileUpdateRequest;
import athletia.model.response.UserResponse;
import athletia.util.Gender;
import athletia.util.Goal;
import athletia.util.TrainingLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(UserEntrypoint.class)
@DisplayName("UserControllerTest – Testes de endpoint REST")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserController controller;

    private String updateProfileJson;
    private UserResponse expectedResponse;

    @BeforeEach
    void setUp() {
        updateProfileJson = """
        {
          "height": 1.80,
          "weight": 82.5,
          "birthDate": "1995-06-15",
          "gender": "MALE",
          "level": "INTERMEDIATE",
          "goal": "WEIGHT_LOSS"
        }
        """;

        expectedResponse = new UserResponse(
                "1",
                "John Doe",
                "johndoe",
                "john.doe@example.com",
                1.80,
                82.5,
                LocalDate.of(1995, 6, 15),
                Gender.MALE,
                TrainingLevel.INTERMEDIATE,
                Goal.WEIGHT_LOSS,
                Instant.parse("2025-05-14T00:00:00Z")
        );

        when(controller.updateAuthenticatedUserProfile(any(UserProfileUpdateRequest.class)))
                .thenReturn(expectedResponse);

        when(controller.getAuthenticatedUserProfile())
                .thenReturn(expectedResponse);
    }

    @Test
    @DisplayName("PUT /users/me – Atualização de perfil com dados válidos retorna 200 e payload correto")
    void whenPutUpdateProfile_withValidInput_thenReturns200AndResponse() throws Exception {
        mockMvc.perform(put("/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateProfileJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.height").value(1.80))
                .andExpect(jsonPath("$.weight").value(82.5))
                .andExpect(jsonPath("$.birthDate").value("1995-06-15"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andExpect(jsonPath("$.level").value("INTERMEDIATE"));
    }

    @Test
    @DisplayName("PUT /users/me – Payload inválido retorna 400 Bad Request")
    void whenPutUpdateProfile_withInvalidInput_thenReturns400() throws Exception {
        var invalidJson = """
        {
          "height": -10,
          "weight": null,
          "birthDate": "invalid-date",
          "gender": null,
          "level": null
        }
        """;

        mockMvc.perform(put("/users/me")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /users/me – Retorna dados do usuário autenticado")
    void whenGetMyProfile_thenReturns200() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @DisplayName("GET /users/me – Quando usuário não encontrado, retorna 500 Internal Server Error")
    void whenGetMyProfile_notFound_thenReturns500() throws Exception {
        when(controller.getAuthenticatedUserProfile())
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }
}
