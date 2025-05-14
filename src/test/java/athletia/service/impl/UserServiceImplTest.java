package athletia.service.impl;

import athletia.config.mapper.GenericMapper;
import athletia.model.Gender;
import athletia.model.TrainingLevel;
import athletia.model.User;
import athletia.model.request.UserRequest;
import athletia.model.response.UserResponse;
import athletia.repository.UserRepository;
import athletia.validations.UserValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("UserServiceImplTest – Testes unitários do service")
class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private GenericMapper mapper;

    @Mock
    private UserValidations validations;

    @InjectMocks
    private UserServiceImpl service;

    private UserRequest validRequest;
    private User userEntity;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        validRequest = new UserRequest(
                "Alice",
                "alice",
                "alice@example.com",
                "pass123",
                1.65,
                60.0,
                LocalDate.of(1998, 4, 22),
                Gender.FEMALE,
                TrainingLevel.BEGINNER
        );

        userEntity = User.builder()
                .id("42")
                .name("Alice")
                .username("alice")
                .email("alice@example.com")
                .password("pass123")
                .height(1.65)
                .weight(60.0)
                .birthDate(LocalDate.of(1998, 4, 22))
                .gender(Gender.FEMALE)
                .level(TrainingLevel.BEGINNER)
                .createdAt(Instant.parse("2025-05-14T00:00:00Z"))
                .build();

        userResponse = new UserResponse(
                "42",
                "Alice",
                "alice",
                "alice@example.com",
                1.65,
                60.0,
                LocalDate.of(1998, 4, 22),
                Gender.FEMALE,
                TrainingLevel.BEGINNER,
                Instant.parse("2025-05-14T00:00:00Z")
        );

        doNothing().when(validations)
                .validateEmailAndUsernameUniqueness(validRequest.email(), validRequest.username());
        doNothing().when(validations)
                .validatePassword(validRequest.password());
        when(repository.save(any(User.class)))
                .thenReturn(userEntity);
        when(mapper.map(userEntity, UserResponse.class))
                .thenReturn(userResponse);

        when(repository.findById("42"))
                .thenReturn(Optional.of(userEntity));
        when(mapper.map(userEntity, UserResponse.class))
                .thenReturn(userResponse);
    }

    @Test
    @DisplayName("createUser – Quando dados válidos, retorna UserResponse e chama dependências")
    void createUser_withValidRequest_shouldReturnResponse() {
        UserResponse result = service.createUser(validRequest);

        assertSame(userResponse, result);

        verify(validations).validateEmailAndUsernameUniqueness("alice@example.com", "alice");
        verify(validations).validatePassword("pass123");
        verify(repository).save(any(User.class));
        verify(mapper).map(userEntity, UserResponse.class);
    }

    @Test
    @DisplayName("createUser – Quando email ou username já existir, lança RuntimeException")
    void createUser_withDuplicateEmailOrUsername_shouldThrow() {
        doThrow(new RuntimeException("duplicate"))
                .when(validations).validateEmailAndUsernameUniqueness(validRequest.email(), validRequest.username());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createUser(validRequest));
        assertEquals("duplicate", ex.getMessage());

        verify(repository, never()).save(any());
        verify(mapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("createUser – Quando senha inválida, lança RuntimeException")
    void createUser_withInvalidPassword_shouldThrow() {
        doThrow(new RuntimeException("invalid password"))
                .when(validations).validatePassword(validRequest.password());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createUser(validRequest));
        assertEquals("invalid password", ex.getMessage());

        verify(repository, never()).save(any());
        verify(mapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("getUserById – Quando usuário existe, retorna UserResponse")
    void getUserById_withExistingUser_shouldReturnResponse() {
        UserResponse result = service.getUserById("42");

        assertSame(userResponse, result);
        verify(repository).findById("42");
        verify(mapper).map(userEntity, UserResponse.class);
    }

    @Test
    @DisplayName("getUserById – Quando usuário não existe, lança RuntimeException")
    void getUserById_withNonExistingUser_shouldThrow() {
        when(repository.findById("99"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getUserById("99"));
        assertEquals("User not found", ex.getMessage());
        verify(mapper, never()).map(any(), any());
    }
}
