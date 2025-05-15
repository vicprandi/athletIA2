package athletia.service;


import athletia.model.User;
import athletia.model.request.UserProfileUpdateRequest;
import athletia.model.request.UserRequest;
import athletia.model.response.UserResponse;

public interface UserService {

    UserResponse getUserById(String userId);

    UserResponse getAuthenticatedUser();

    UserResponse updateAuthenticatedUser(UserProfileUpdateRequest request);

    User getFullUserEntityById(String userId);

}
