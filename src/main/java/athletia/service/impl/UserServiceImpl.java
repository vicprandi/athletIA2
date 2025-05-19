package athletia.service.impl;

import athletia.config.mapper.GenericMapper;
import athletia.model.User;
import athletia.model.request.UserProfileUpdateRequest;
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
    private final CurrentUserService currentUserService;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository repository, GenericMapper mapper, CurrentUserService currentUserService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.currentUserService = currentUserService;
        this.passwordEncoder = passwordEncoder;
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
    public UserResponse updateAuthenticatedUser(UserProfileUpdateRequest request) {
        String userId = currentUserService.getCurrentUserId();
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User updated = user.toBuilder()
                .height(request.height())
                .weight(request.weight())
                .birthDate(request.birthDate())
                .gender(request.gender())
                .level(request.level())
                .goal(request.goal())
                .createdAt(user.createdAt())
                .build();

        return mapper.map(repository.save(updated), UserResponse.class);
    }

    @Override
    public User getFullUserEntityById(String userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
