package athletia.validations;

import athletia.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidations {

    private final UserRepository repository;

    public UserValidations(UserRepository repository) {
        this.repository = repository;
    }

    public void validateEmailAndUsernameUniqueness(String email, String username) {
        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email taken.");
        }

        if (repository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username taken.");
        }
    }

    public void validatePassword(String password) {
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");

        if (!(hasUppercase && hasLowercase && hasSpecial)) {
            throw new IllegalArgumentException("Password must have uppercase, lowercase and special characters.");
        }
    }
}
