package athletia.service.impl;

import athletia.config.mapper.GenericMapper;
import athletia.model.User;
import athletia.model.request.UserProfileUpdateRequest;
import athletia.model.response.UserResponse;
import athletia.repository.UserRepository;
import athletia.service.CurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import athletia.util.Gender;
import athletia.util.TrainingLevel;

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
    private CurrentUserService currentUserService;

    @InjectMocks
    private UserServiceImpl service;

    private User userEntity;
    private UserResponse userResponse;
    private UserProfileUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        userEntity = User.builder()
                .id("42")
                .name("Alice")
                .username("alice")
                .email("alice@example.com")
                .password("encodedPassword")
                .height(1.60)
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
                1.60,
                60.0,
                LocalDate.of(1998, 4, 22),
                Gender.FEMALE,
                TrainingLevel.BEGINNER,
                Instant.parse("2025-05-14T00:00:00Z")
        );

        updateRequest = new UserProfileUpdateRequest(
                1.60,
                60.0,
                LocalDate.of(1998, 4, 22),
                Gender.FEMALE,
                TrainingLevel.BEGINNER
        );

        when(currentUserService.getCurrentUserId()).thenReturn("42");
        when(repository.findById("42")).thenReturn(Optional.of(userEntity));
        when(repository.save(any(User.class))).thenReturn(userEntity);
        when(mapper.map(userEntity, UserResponse.class)).thenReturn(userResponse);
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
        when(repository.findById("99")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getUserById("99"));
        assertEquals("User not found", ex.getMessage());
        verify(mapper, never()).map(any(), any());
    }

    @Test
    @DisplayName("updateAuthenticatedUser – Quando usuário logado existe, atualiza e retorna UserResponse")
    void updateAuthenticatedUser_shouldUpdateAndReturn() {
        UserResponse result = service.updateAuthenticatedUser(updateRequest);

        assertSame(userResponse, result);
        verify(repository).findById("42");
        verify(repository).save(any(User.class));
        verify(mapper).map(any(User.class), eq(UserResponse.class));
    }
}
