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

    public void validateEmailUniquenessOnUpdate(String email, String currentUserId) {
        boolean emailUsedByAnother = repository.existsByEmailAndIdNot(email, currentUserId);
        if (emailUsedByAnother) {
            throw new IllegalArgumentException("Email already in use by another user.");
        }
    }

    public void validateUsernameUniquenessOnUpdate(String username, String currentUserId) {
        boolean usernameUsedByAnother = repository.existsByUsernameAndIdNot(username, currentUserId);
        if (usernameUsedByAnother) {
            throw new IllegalArgumentException("Username already in use by another user.");
        }
    }


}
