package pl.wiktor.ostatniaseria.infrastucture.database.jpa.token;

import org.springframework.core.convert.ConversionService;
import org.springframework.transaction.annotation.Transactional;
import pl.wiktor.ostatniaseria.domain.token.Token;
import pl.wiktor.ostatniaseria.domain.token.TokenRepository;

import java.util.Objects;
import java.util.Optional;

public class TokenRepositoryPersistence implements TokenRepository {
    private final TokenRepositoryInterface database;
    private final ConversionService conversionService;

    public TokenRepositoryPersistence(TokenRepositoryInterface database, ConversionService conversionService) {
        this.database = database;
        this.conversionService = conversionService;
    }

    @Transactional
    @Override
    public void saveOrRefreshToken(Token token) {
        database.deleteByEmail(token.email());
        database.save(Objects.requireNonNull(conversionService.convert(token, TokenJPA.class)));
    }

    @Override
    public Optional<Token> getToken(String tokenUuid) {
        return database.getByUuid(tokenUuid);
    }
}
