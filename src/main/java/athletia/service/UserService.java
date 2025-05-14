package athletia.service;


import athletia.model.request.UserRequest;
import athletia.model.response.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse getUserById(String userId);
}
