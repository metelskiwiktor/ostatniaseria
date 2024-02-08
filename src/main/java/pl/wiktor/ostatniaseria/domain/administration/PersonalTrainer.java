package pl.wiktor.ostatniaseria.domain.administration;

import pl.wiktor.ostatniaseria.domain.user.model.User;

import java.util.List;

public record PersonalTrainer(User owner, List<User> users) {
}
