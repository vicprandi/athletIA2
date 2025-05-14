package athletia.service.impl;

import athletia.config.mapper.GenericMapper;
import athletia.model.User;
import athletia.model.request.UserRequest;
import athletia.model.response.UserResponse;
import athletia.repository.UserRepository;
import athletia.service.UserService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final GenericMapper mapper;

    public UserServiceImpl(UserRepository repository, GenericMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserResponse createUser(UserRequest request){
        repository.findByEmail(request.email()).ifPresent(u -> {
            throw new RuntimeException("Já tem esse email");
        });

        User user = User.builder()
                .name(request.name())
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
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return mapper.map(user, UserResponse.class);
    }
}
