package pl.wiktor.ostatniaseria.domain.user;

import pl.wiktor.ostatniaseria.domain.user.model.register.User;

public interface UserRepository {
    void createUser(User user);

    boolean isPasswordOrUserExists(String email, String password);

    boolean isEmailInUse(String email);

    User getUserByEmail(String email);
}
