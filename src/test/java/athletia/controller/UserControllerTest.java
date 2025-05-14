package athletia.controller;

import athletia.config.security.authentication.JwtService;
import athletia.entrypoint.UserEntrypoint;
import athletia.model.request.UserRequest;
import athletia.model.response.UserResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private String validUserJson;
    private UserResponse expectedResponse;

    @BeforeEach
    void setUp() {
        validUserJson = """
            {
              "name": "John Doe",
              "username": "johndoe",
              "email": "john.doe@example.com",
              "password": "password123"
            }
            """;

        expectedResponse = new UserResponse(
                "1",
                "John Doe",
                "johndoe",
                "john.doe@example.com",
                Instant.parse("2025-05-14T00:00:00Z")
        );

        when(controller.create(any(UserRequest.class)))
                .thenReturn(expectedResponse);
    }

    @Test
    @DisplayName("POST /users – Criar usuário com dados válidos retorna 201 e payload correto")
    void whenPostCreateUser_withValidInput_thenReturns201AndResponseBody() throws Exception {
        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUserJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.createdAt").value("2025-05-14T00:00:00Z"));
    }

    @Test
    @DisplayName("POST /users – Payload inválido retorna 400 Bad Request")
    void whenPostCreateUser_withInvalidInput_thenReturns400() throws Exception {
        var invalidJson = """
            {
              "name": "",
              "username": "johndoe",
              "email": "not-an-email",
              "password": ""
            }
            """;

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /users/{userId} – Usuário existente retorna 200 e payload correto")
    void whenGetUserById_withExistingUser_thenReturns200AndResponseBody() throws Exception {
        // Mock do findById
        when(controller.findById("1"))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/users/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.username").value("johndoe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.createdAt").value("2025-05-14T00:00:00Z"));
    }

    @Test
    @DisplayName("GET /users/{userId} – Usuário não existe retorna 500 Internal Server Error")
    void whenGetUserById_withNonExistingUser_thenReturns500() throws Exception {
        // O GlobalExceptionHandler converte essa exceção num 500
        when(controller.findById("42"))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/users/{id}", "42"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }
}