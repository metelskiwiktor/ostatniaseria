package pl.wiktor.ostatniaseria.infrastucture.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.wiktor.ostatniaseria.api.security.AuthInterceptor;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.user.UserRepositoryInterface;
import pl.wiktor.ostatniaseria.infrastucture.mapper.*;

@Configuration
@DependsOn("applicationConfig")
public class WebConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final UserRepositoryInterface userRepositoryInterface;
    @Lazy
    @Autowired
    private ConversionService conversionService;

    public WebConfig(AuthInterceptor authInterceptor, UserRepositoryInterface userRepositoryInterface) {
        this.authInterceptor = authInterceptor;
        this.userRepositoryInterface = userRepositoryInterface;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new TokenToTokenJPA());
        registry.addConverter(new MeetingToMeetingJPA());
        registry.addConverter(new UserJPAToUser());
        registry.addConverter(new TrainingReportToTrainingReportJPA(conversionService));
        registry.addConverter(new UserToUserJPA(userRepositoryInterface));
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
    }
}
