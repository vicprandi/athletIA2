package athletia.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(

        @NotBlank(message = "Please insert a name")
        String name,

        String username,

        @NotBlank(message = "Please insert a email")
        @Email
        String email,

        @NotBlank(message = "Please insert a password")
        String password) {}

