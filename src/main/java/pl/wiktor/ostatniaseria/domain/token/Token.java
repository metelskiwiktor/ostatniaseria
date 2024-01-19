package pl.wiktor.ostatniaseria.domain.token;

import java.time.Instant;

public record Token(String email, String uuid, Instant expirationDate) {

}
