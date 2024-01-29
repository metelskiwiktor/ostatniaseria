package pl.wiktor.ostatniaseria.infrastucture.mapper;

import org.springframework.core.convert.converter.Converter;
import pl.wiktor.ostatniaseria.domain.user.model.register.User;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.user.UserJPA;

public class UserJPAToUser implements Converter<UserJPA, User> {
    @Override
    public User convert(UserJPA source) {
        return new User(source.getEmail(), source.getPassword());
    }
}
