package pl.wiktor.ostatniaseria.infrastucture.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.wiktor.ostatniaseria.api.security.AuthInterceptor;
import pl.wiktor.ostatniaseria.infrastucture.mapper.CreateUserToUserJPA;
import pl.wiktor.ostatniaseria.infrastucture.mapper.MeetingToMeetingJPA;
import pl.wiktor.ostatniaseria.infrastucture.mapper.TokenToTokenJPA;

@Configuration
@DependsOn("applicationConfig")
public class WebConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new CreateUserToUserJPA());
        registry.addConverter(new TokenToTokenJPA());
        registry.addConverter(new MeetingToMeetingJPA());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }
}
