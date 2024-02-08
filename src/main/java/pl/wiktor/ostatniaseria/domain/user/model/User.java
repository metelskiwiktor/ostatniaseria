package pl.wiktor.ostatniaseria.domain.user.model;

import pl.wiktor.ostatniaseria.domain.administration.PersonalTrainer;

public record User(String username, String email, String password, PersonalTrainer personalTrainer) {
    @Override
    public String toString() {
        return "User[" +
                "username=" + username + ", " +
                "email=" + email + ", " +
                "password=" + password + ", " +
                "personalTrainer=" + personalTrainer + ']';
    }

}
