package athletia.service.impl;

import athletia.config.mapper.GenericMapper;
import athletia.model.User;
import athletia.model.request.UserRequest;
import athletia.model.response.UserResponse;
import athletia.repository.UserRepository;
import athletia.service.UserService;
import athletia.validations.UserValidations;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final GenericMapper mapper;
    private final UserValidations validations;

    public UserServiceImpl(UserRepository repository, GenericMapper mapper, UserValidations validations) {
        this.repository = repository;
        this.mapper = mapper;
        this.validations = validations;
    }

    @Override
    public UserResponse createUser(UserRequest request){

        validations.validateEmailAndUsernameUniqueness(request.email(), request.username());
        validations.validatePassword(request.password());

        User user = User.builder()
                .name(request.name())
                .username(request.username())
                .email(request.email())
                .password(request.password())
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
}
