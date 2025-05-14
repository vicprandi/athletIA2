package athletia.model.request;

import athletia.model.Gender;
import athletia.model.TrainingLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserRequest(

        @NotBlank(message = "Please insert a name")
        String name,

        String username,

        @NotBlank(message = "Please insert a email")
        @Email
        String email,

        @NotBlank(message = "Please insert a password")
        String password,

        @NotNull(message = "Please insert height")
        Double height,

        @NotNull(message = "Please insert weight")
        Double weight,

        @NotNull(message = "Please insert birth date")
        LocalDate birthDate,

        @NotNull(message = "Please insert gender")
        Gender gender,

        @NotNull(message = "Please insert training level")
        TrainingLevel level
) {}

