package athletia.entrypoint;

import athletia.controller.UserController;
import athletia.model.request.UserProfileUpdateRequest;
import athletia.model.request.UserRequest;
import athletia.model.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Users")
public class UserEntrypoint {

    private final UserController controller;

    public UserEntrypoint(UserController controller) {
        this.controller = controller;
    }

    @Operation(summary = "Search for userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public UserResponse findById(@Valid @PathVariable(name = "userId")  String userId){
        return controller.findById(userId);
    }

    @Operation(summary = "Get profile of authenticated user")
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getMyProfile() {
        return controller.getAuthenticatedUserProfile();
    }

    @Operation(summary = "Update profile of authenticated user")
    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateMyProfile(@Valid @RequestBody UserProfileUpdateRequest request) {
        return controller.updateAuthenticatedUserProfile(request);
    }
}
