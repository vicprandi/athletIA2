package athletia.controller;

import athletia.model.request.UserRequest;
import athletia.model.response.UserResponse;
import athletia.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    public UserResponse create(UserRequest request){
        return service.createUser(request);
    }

    public UserResponse findById(String userId){
        return service.getUserById(userId);
    }
}
