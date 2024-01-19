package pl.wiktor.ostatniaseria.domain.token;

import java.util.Optional;

public interface TokenRepository {
    void saveOrRefreshToken(Token token);

    Optional<Token> getToken(String tokenUuid);
}
