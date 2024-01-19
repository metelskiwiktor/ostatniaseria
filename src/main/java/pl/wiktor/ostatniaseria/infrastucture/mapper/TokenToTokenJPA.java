package pl.wiktor.ostatniaseria.infrastucture.mapper;

import org.springframework.core.convert.converter.Converter;
import pl.wiktor.ostatniaseria.domain.token.Token;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.token.TokenJPA;

public class TokenToTokenJPA implements Converter<Token, TokenJPA> {
    @Override
    public TokenJPA convert(Token source) {
        TokenJPA tokenJPA = new TokenJPA();
        tokenJPA.setEmail(source.email());
        tokenJPA.setUuid(source.uuid());
        tokenJPA.setExpirationDate(source.expirationDate());
        return tokenJPA;
    }
}
