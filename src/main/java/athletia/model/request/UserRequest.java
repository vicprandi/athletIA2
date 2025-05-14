package athletia.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(

        @NotBlank(message = "Nome obrigatorio")
        String name,

        @NotBlank(message = "Email obrigatorio")
        @Email
        String email,

        @NotBlank(message = "Senha obrigatória")
        String password) {}

