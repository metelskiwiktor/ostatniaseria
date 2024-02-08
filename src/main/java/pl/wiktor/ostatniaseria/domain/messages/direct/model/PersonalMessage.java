package pl.wiktor.ostatniaseria.domain.messages.direct.model;

import pl.wiktor.ostatniaseria.domain.model.Media;
import pl.wiktor.ostatniaseria.domain.user.model.User;

import java.time.ZonedDateTime;

public record PersonalMessage(User from, ZonedDateTime sentAt, String message, Media media) {
}
