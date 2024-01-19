package pl.wiktor.ostatniaseria.infrastucture.database.jpa.user;

import org.springframework.core.convert.ConversionService;
import pl.wiktor.ostatniaseria.domain.user.UserRepository;
import pl.wiktor.ostatniaseria.domain.user.model.register.User;

import java.util.Objects;

public class UserRepositoryPersistence implements UserRepository {
    private final UserRepositoryInterface database;
    private final ConversionService conversionService;

    public UserRepositoryPersistence(UserRepositoryInterface database, ConversionService conversionService) {
        this.database = database;
        this.conversionService = conversionService;
    }

    @Override
    public void createUser(User user) {
        database.save(Objects.requireNonNull(conversionService.convert(user, UserJPA.class)));
    }

    @Override
    public String login(String email, String password) {
        return null;
    }

    @Override
    public boolean isPasswordOrUserExists(String email, String password) {
        return database.existsByEmailAndPassword(email, password);
    }

    @Override
    public boolean isEmailInUse(String email) {
        return database.existsByEmail(email);
    }
}
