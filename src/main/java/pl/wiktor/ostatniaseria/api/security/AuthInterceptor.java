package pl.wiktor.ostatniaseria.api.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import pl.wiktor.ostatniaseria.domain.token.TokenService;

import java.util.Objects;

public class AuthInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    public AuthInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        RequiresLogin loginRequired = handlerMethod.getMethodAnnotation(RequiresLogin.class);

        if (loginRequired != null) {
            String authorization = request.getHeader("Authorization");
            if (Objects.isNull(authorization)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return false;
            }
            String token = authorization.substring(7);
            boolean valid = tokenService.checkToken(token);

            if (!valid) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return false;
            }

            // Extract user from token and set as request attribute
            request.setAttribute("user", tokenService.getUser(token));
        }

        return true;
    }
}