package pl.wiktor.ostatniaseria.infrastucture.mapper;

import org.springframework.core.convert.converter.Converter;
import pl.wiktor.ostatniaseria.domain.user.model.register.User;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.user.UserJPA;

public class CreateUserToUserJPA implements Converter<User, UserJPA> {
    @Override
    public UserJPA convert(User source) {
        UserJPA userJPA = new UserJPA();
        userJPA.setPassword(source.password());
        userJPA.setEmail(source.email());
        return userJPA;
    }
}
