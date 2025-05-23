package athletia.controller;

import athletia.model.request.UserProfileUpdateRequest;
import athletia.model.response.UserResponse;
import athletia.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    public UserResponse findById(String userId){
        return service.getUserById(userId);
    }

    public UserResponse getAuthenticatedUserProfile() {
        return service.getAuthenticatedUser();
    }

    public UserResponse updateAuthenticatedUserProfile(UserProfileUpdateRequest request) {
        return service.updateAuthenticatedUser(request);
    }
}
