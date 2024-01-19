package pl.wiktor.ostatniaseria.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.wiktor.ostatniaseria.api.model.request.LoginUserRequest;
import pl.wiktor.ostatniaseria.api.model.request.RegisterUserRequest;
import pl.wiktor.ostatniaseria.api.security.RequiresLogin;
import pl.wiktor.ostatniaseria.domain.token.TokenService;
import pl.wiktor.ostatniaseria.domain.user.UserService;

import java.time.Clock;

@RestController
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;
    private final Clock clock;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, TokenService tokenService, Clock clock) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.clock = clock;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterUserRequest createUserRequest) {
        LOGGER.info("Creating user with email: {}", createUserRequest.email);
        userService.createUser(
                createUserRequest.email,
                createUserRequest.password
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginUserRequest loginUserRequest) {
        return tokenService.login(
                loginUserRequest.email,
                loginUserRequest.password
        );
    }

    //    @RequiresLogin
    @GetMapping
    public String getCurrentTime() {
        return clock.instant().toString();
    }
}
