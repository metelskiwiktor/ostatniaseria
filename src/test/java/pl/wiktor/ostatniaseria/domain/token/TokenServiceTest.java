package pl.wiktor.ostatniaseria.domain.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wiktor.ostatniaseria.domain.exception.DomainException;
import pl.wiktor.ostatniaseria.domain.user.UserService;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TokenRepository tokenRepository;

    private TokenService tokenService;

    @BeforeEach
    public void setup() {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        this.tokenService = new TokenService(userService, tokenRepository, clock);
    }

    @Test
    public void loginShouldReturnUuidOnSuccessfulLogin() {
        //given
        String email = "test@test.com";
        String password = "Password123!";
        when(userService.verifyPasswordOrUserExists(email, password)).thenReturn(true);
        //when
        String result = tokenService.login(email, password);
        //then
        assertNotNull(result);
        verify(tokenRepository, times(1)).saveOrRefreshToken(any());
    }

    @Test
    public void loginShouldThrowExceptionIfUserIsInvalid() {
        //given
        String email = "test@test.com";
        String password = "Password123!";
        when(userService.verifyPasswordOrUserExists(email, password)).thenReturn(false);
        //when //then
        assertThrows(DomainException.class, () -> tokenService.login(email, password));
    }

    @Test
    public void checkTokenShouldReturnTrueIfTokenIsValid() {
        //given
        String uuid = "some-uuid";
        Token token = new Token("test@test.com", uuid, Instant.now().plusSeconds(60));
        when(tokenRepository.getToken(uuid)).thenReturn(java.util.Optional.of(token));
        //when
        boolean result = tokenService.checkToken(uuid);
        //then
        assertTrue(result);
        verify(tokenRepository, times(1)).getToken(uuid);
    }

    @Test
    public void checkTokenShouldReturnFalseIfTokenIsNotPresent() {
        //given
        String uuid = "some-uuid";
        when(tokenRepository.getToken(uuid)).thenReturn(java.util.Optional.empty());
        //when
        boolean result = tokenService.checkToken(uuid);
        //then
        assertFalse(result);
        verify(tokenRepository, times(1)).getToken(uuid);
    }

    @Test
    public void checkTokenShouldReturnFalseIfTokenIsExpired() {
        //given
        String uuid = "some-uuid";
        Token token = new Token("test@test.com", uuid, Instant.now().minusSeconds(60)); // Already expired token
        when(tokenRepository.getToken(uuid)).thenReturn(java.util.Optional.of(token));
        //when
        boolean result = tokenService.checkToken(uuid);
        //then
        assertFalse(result);
        verify(tokenRepository, times(1)).getToken(uuid);
    }
}
