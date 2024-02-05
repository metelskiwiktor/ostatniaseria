package pl.wiktor.ostatniaseria.domain.user;

import pl.wiktor.ostatniaseria.domain.user.model.User;

public interface UserRepository {
    void createUser(User user);

    boolean isPasswordOrUserExists(String email, String password);

    boolean isEmailInUse(String email);

    User getUserByEmail(String email);

    boolean isUsernameInUse(String username);
}
