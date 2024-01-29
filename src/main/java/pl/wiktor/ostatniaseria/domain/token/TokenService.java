package pl.wiktor.ostatniaseria.domain.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.wiktor.ostatniaseria.domain.exception.DomainException;
import pl.wiktor.ostatniaseria.domain.exception.ErrorCode;
import pl.wiktor.ostatniaseria.domain.lib.PasswordHash;
import pl.wiktor.ostatniaseria.domain.user.UserService;
import pl.wiktor.ostatniaseria.domain.user.model.register.User;

import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.function.Supplier;

public class TokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final Clock clock;

    public TokenService(UserService userService, TokenRepository tokenRepository, Clock clock) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.clock = clock;
    }

    public String login(String email, String password) {
        Validator.checkUserExists(email, () -> userService.verifyPasswordOrUserExists(email, PasswordHash.hashPassword(password)));
        Token token = new Token(email, UUID.randomUUID().toString(), clock.instant().plus(5, ChronoUnit.DAYS));
        tokenRepository.saveOrRefreshToken(token);
        LOGGER.info("User with email {} has been logged in", email);
        return token.uuid();
    }

    public boolean checkToken(String token) {
        return tokenRepository.getToken(token)
                .map(value -> value.expirationDate().isAfter(clock.instant()))
                .orElse(false);
    }

    public User getUser(String token) {
        return tokenRepository.getToken(token)
                .map(Token::email)
                .map(userService::getUserByEmail)
                .orElseThrow(() -> new DomainException(ErrorCode.INVALID_TOKEN, token));
    }

    static class Validator {
        public static void checkUserExists(String email, Supplier<Boolean> action) {
            if (!action.get()) {
                throw new DomainException(ErrorCode.INVALID_PASSWORD_OR_EMAIL_NOT_FOUND, email);
            }
        }
    }
}
