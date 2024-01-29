package pl.wiktor.ostatniaseria.infrastucture.mapper;

import org.springframework.core.convert.converter.Converter;
import pl.wiktor.ostatniaseria.domain.user.model.register.User;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.user.UserJPA;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.user.UserRepositoryInterface;

import java.util.Optional;

public class UserToUserJPA implements Converter<User, UserJPA> {
    private final UserRepositoryInterface database;

    public UserToUserJPA(UserRepositoryInterface database) {
        this.database = database;
    }

    @Override
    public UserJPA convert(User source) {
        UserJPA userJPA = new UserJPA();
        userJPA.setEmail(source.email());
        userJPA.setPassword(source.password());
        Optional.ofNullable(database.getByEmail(source.email()))
                .ifPresent(jpa -> userJPA.setId(jpa.getId()));
        return userJPA;
    }
}
