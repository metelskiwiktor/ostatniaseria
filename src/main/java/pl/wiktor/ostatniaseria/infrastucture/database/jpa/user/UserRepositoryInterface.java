package pl.wiktor.ostatniaseria.infrastucture.database.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryInterface extends JpaRepository<UserJPA, String> {
    boolean existsByEmailAndPassword(String email, String password);
    boolean existsByEmail(String email);
    UserJPA getByEmail(String email);
}
