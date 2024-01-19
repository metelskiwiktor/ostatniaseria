package pl.wiktor.ostatniaseria.infrastucture.database.jpa.token;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wiktor.ostatniaseria.domain.token.Token;

import java.util.Optional;

public interface TokenRepositoryInterface extends JpaRepository<TokenJPA, String> {
    void deleteByEmail(String email);
    Optional<Token> getByUuid(String uuid);
}
