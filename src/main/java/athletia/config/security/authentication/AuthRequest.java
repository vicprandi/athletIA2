package athletia.config.security.authentication;

public record AuthRequest(
        String email,
        String password
) {
}
