package athletia.entrypoint;

import athletia.controller.UserController;
import athletia.model.request.UserRequest;
import athletia.model.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserEntrypoint {

    private final UserController controller;

    public UserEntrypoint(UserController controller) {
        this.controller = controller;
    }

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponse create(@Valid @RequestBody UserRequest request){
        return controller.create(request);
    }

    @Operation(summary = "Search for userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public UserResponse findById(@Valid @RequestParam String userId){
        return controller.findById(userId);
    }
}
