package athletia.service.impl;

import athletia.config.mapper.GenericMapper;
import athletia.model.User;
import athletia.model.request.UserRequest;
import athletia.model.response.UserResponse;
import athletia.repository.UserRepository;
import athletia.service.CurrentUserService;
import athletia.service.UserService;
import athletia.validations.UserValidations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final GenericMapper mapper;
    private final UserValidations validations;
    private final CurrentUserService currentUserService;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository repository, GenericMapper mapper, UserValidations validations, CurrentUserService currentUserService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.validations = validations;
        this.currentUserService = currentUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse createUser(UserRequest request){

        validations.validateEmailAndUsernameUniqueness(request.email(), request.username());
        validations.validatePassword(request.password());

        User user = User.builder()
                .name(request.name())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .height(request.height())
                .weight(request.weight())
                .birthDate(request.birthDate())
                .gender(request.gender())
                .level(request.level())
                .createdAt(Instant.now())
                .build();

        User saved = repository.save(user);
        return  mapper.map(saved, UserResponse.class);
    }

    @Override
    public UserResponse getUserById(String userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapper.map(user, UserResponse.class);
    }


    @Override
    public UserResponse getAuthenticatedUser() {
        String userId = currentUserService.getCurrentUserId();
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse updateAuthenticatedUser(UserRequest request) {
        String userId = currentUserService.getCurrentUserId();
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        validations.validateEmailUniquenessOnUpdate(request.email(), userId);
        validations.validateUsernameUniquenessOnUpdate(request.username(), userId);
        validations.validatePassword(request.password());

        User updated = User.builder()
                .id(user.id())
                .name(request.name())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .height(request.height())
                .weight(request.weight())
                .birthDate(request.birthDate())
                .gender(request.gender())
                .level(request.level())
                .createdAt(user.createdAt())
                .build();

        return mapper.map(repository.save(updated), UserResponse.class);
    }
}
