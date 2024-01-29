package pl.wiktor.ostatniaseria.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.wiktor.ostatniaseria.domain.exception.DomainException;
import pl.wiktor.ostatniaseria.domain.exception.ErrorCode;
import pl.wiktor.ostatniaseria.domain.lib.PasswordHash;
import pl.wiktor.ostatniaseria.domain.user.model.register.User;

import java.util.function.Supplier;
import java.util.regex.Pattern;

public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9]+)(?=.*[a-zA-Z]{5,})(?=.*[!@#$%^&*()_+=]+).*$");

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(String email, String password) {
        Validator.verifyEmailAlreadyRegistered(email, () -> userRepository.isEmailInUse(email));
        Validator.verifyEmailSyntax(email);
        Validator.verifyStrongPassword(password);
        userRepository.createUser(new User(email, PasswordHash.hashPassword(password)));
        LOGGER.info("User {} has been created", email);
    }

    public boolean verifyPasswordOrUserExists(String email, String password) {
        return userRepository.isPasswordOrUserExists(email, PasswordHash.hashPassword(password));
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    static class Validator {
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