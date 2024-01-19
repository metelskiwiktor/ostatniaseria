package pl.wiktor.ostatniaseria.infrastucture.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import pl.wiktor.ostatniaseria.api.security.AuthInterceptor;
import pl.wiktor.ostatniaseria.domain.token.TokenRepository;
import pl.wiktor.ostatniaseria.domain.token.TokenService;
import pl.wiktor.ostatniaseria.domain.user.UserService;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.token.TokenRepositoryInterface;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.token.TokenRepositoryPersistence;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.user.UserRepositoryInterface;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.user.UserRepositoryPersistence;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ApplicationConfig {
    @Bean
    public UserService userService(UserRepositoryPersistence database) {
        return new UserService(database);
    }

    @Bean
    public TokenService tokenService(UserService userService, TokenRepository tokenRepository, Clock warsawZone) {
        return new TokenService(userService, tokenRepository, warsawZone);
    }

    @Bean
    public AuthInterceptor authInterceptor(@Lazy TokenService tokenService) {
        return new AuthInterceptor(tokenService);
    }

    @Bean
    public UserRepositoryPersistence userRepositoryPersistence(
            UserRepositoryInterface database, ConversionService conversionService) {
        return new UserRepositoryPersistence(database, conversionService);
    }

    @Bean
    public TokenRepositoryPersistence tokenRepositoryPersistence(
            TokenRepositoryInterface database, ConversionService conversionService) {
        return new TokenRepositoryPersistence(database, conversionService);
    }

    @Bean
    Clock warsawZone() {
        return Clock.system(ZoneId.of("Europe/Warsaw"));
    }
}
