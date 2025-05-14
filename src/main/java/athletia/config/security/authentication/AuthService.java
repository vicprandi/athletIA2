package athletia.config.security.authentication;

import athletia.model.request.SignupRequest;

public interface AuthService {

    AuthResponse login(AuthRequest request);

    void signup(SignupRequest request);

}
