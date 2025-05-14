package athletia.model.request;

public record SignupRequest(
        String name,
        String username,
        String email,
        String password) {}
