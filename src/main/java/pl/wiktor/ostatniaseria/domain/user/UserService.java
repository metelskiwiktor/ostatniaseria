package pl.wiktor.ostatniaseria.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.wiktor.ostatniaseria.domain.administration.PersonalTrainer;
import pl.wiktor.ostatniaseria.domain.administration.PersonalTrainerService;
import pl.wiktor.ostatniaseria.domain.exception.DomainException;
import pl.wiktor.ostatniaseria.domain.exception.ErrorCode;
import pl.wiktor.ostatniaseria.domain.lib.PasswordHash;
import pl.wiktor.ostatniaseria.domain.user.model.User;

import java.util.function.Supplier;
import java.util.regex.Pattern;

public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PersonalTrainerService personalTrainerService;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9]+)(?=.*[a-zA-Z]{5,})(?=.*[!@#$%^&*()_+=]+).*$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^.{4,}$");

    public UserService(UserRepository userRepository, PersonalTrainerService personalTrainerService) {
        this.userRepository = userRepository;
        this.personalTrainerService = personalTrainerService;
    }

    public void createUser(String username, String email, String password) {
        Validator.verifyUsernameSyntax(username);
        Validator.verifyEmailSyntax(email);
        Validator.verifyEmailAlreadyRegistered(email, () -> userRepository.isEmailInUse(email));
        Validator.verifyUsernameAlreadyRegistered(username, () -> userRepository.isUsernameInUse(username));
        Validator.verifyStrongPassword(password);
        PersonalTrainer personalTrainer = personalTrainerService.findPersonalTrainerWithLeastUsers();
        userRepository.createUser(new User(username, email, PasswordHash.hashPassword(password), personalTrainer));
        LOGGER.info("User {} has been created", email);
    }

    public boolean verifyPasswordOrUserExists(String email, String password) {
        return userRepository.isPasswordOrUserExists(email, PasswordHash.hashPassword(password));
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    static class Validator {
        public static void verifyUsernameAlreadyRegistered(String username, Supplier<Boolean> action) {
            if (action.get()) {
                throw new DomainException(ErrorCode.USERNAME_ALREADY_REGISTERED, username);
            }
        }

        static void verifyUsernameSyntax(String username) {
            if (!USERNAME_PATTERN.matcher(username).matches()) {
                throw new DomainException(ErrorCode.USERNAME_SYNTAX_ERROR, username);
            }
        }

        public static void verifyEmailAlreadyRegistered(String email, Supplier<Boolean> action) {
            if (action.get()) {
                throw new DomainException(ErrorCode.EMAIL_ALREADY_REGISTERED, email);
            }
        }

        static void verifyEmailSyntax(String email) {
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                throw new DomainException(ErrorCode.EMAIL_SYNTAX_ERROR, email);
            }
        }

        static void verifyStrongPassword(String password) {
            if (!PASSWORD_PATTERN.matcher(password).matches()) {
                throw new DomainException(ErrorCode.WEAK_PASSWORD, password);
            }
        }
    }
}
