package athletia.service.impl;

import athletia.model.User;
import athletia.config.security.authentication.AuthRequest;
import athletia.config.security.authentication.AuthResponse;
import athletia.model.request.SignupRequest;
import athletia.repository.UserRepository;
import athletia.config.security.authentication.AuthService;
import athletia.config.security.authentication.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository repository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Email not found."));

        if (!passwordEncoder.matches(request.password(), user.password())) {
            throw new IllegalArgumentException("Invalid password.");
        }

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @Override
    public void signup(SignupRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email já está em uso.");
        }

        User user = User.builder()
                .name(request.name())
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .createdAt(Instant.now())
                .build();

        repository.save(user);
    }

}
