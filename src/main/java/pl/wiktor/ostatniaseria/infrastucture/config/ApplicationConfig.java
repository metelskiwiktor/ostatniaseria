package pl.wiktor.ostatniaseria.infrastucture.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import pl.wiktor.ostatniaseria.api.security.AuthInterceptor;
import pl.wiktor.ostatniaseria.domain.report.TrainingReportRepository;
import pl.wiktor.ostatniaseria.domain.report.TrainingReportService;
import pl.wiktor.ostatniaseria.domain.scheduler.CalendarNotification;
import pl.wiktor.ostatniaseria.domain.scheduler.SchedulerRepository;
import pl.wiktor.ostatniaseria.domain.scheduler.SchedulerService;
import pl.wiktor.ostatniaseria.domain.token.TokenRepository;
import pl.wiktor.ostatniaseria.domain.token.TokenService;
import pl.wiktor.ostatniaseria.domain.user.UserService;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.report.TrainingReportRepositoryInterface;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.report.TrainingReportRepositoryPersistence;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.scheduler.SchedulerRepositoryInterface;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.scheduler.SchedulerRepositoryPersistence;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.token.TokenRepositoryInterface;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.token.TokenRepositoryPersistence;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.user.UserRepositoryInterface;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.user.UserRepositoryPersistence;
import pl.wiktor.ostatniaseria.infrastucture.google.CalendarGoogle;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;

@Configuration
public class ApplicationConfig {
    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    @Value("${google.calendar.service.enabled}")
    private boolean googleCalendarEnabled;

    @Bean
    public UserService userService(UserRepositoryPersistence database) {
        return new UserService(database);
    }

    @Bean
    public TokenService tokenService(UserService userService, TokenRepository tokenRepository, Clock warsawZone) {
        return new TokenService(userService, tokenRepository, warsawZone);
    }

    @Bean
    public SchedulerService service(SchedulerRepository schedulerRepository, Clock clock, CalendarNotification calendarNotification) {
        return new SchedulerService(schedulerRepository, clock, LocalTime.of(17, 0), LocalTime.of(22, 0), calendarNotification);
    }

    @Bean
    public TrainingReportService trainingReportService(TrainingReportRepository database) {
        return new TrainingReportService(database);
    }

    @Bean
    public TrainingReportRepositoryPersistence trainingReportRepositoryPersistence(
        TrainingReportRepositoryInterface database, ConversionService conversionService
    ) {
        return new TrainingReportRepositoryPersistence(database, conversionService);
    }

    @Bean
    public AuthInterceptor authInterceptor(@Lazy TokenService tokenService) {
        return new AuthInterceptor(tokenService);
    }

    @Bean
    public CalendarNotification calendarNotification() throws GeneralSecurityException, IOException {
        if (googleCalendarEnabled) {
            return new CalendarGoogle();
        } else {
            return (startDate, endDate, title, description) -> LOGGER.debug("Calendar notification is disabled");
        }
    }

    @Bean
    public UserRepositoryPersistence userRepositoryPersistence(
            UserRepositoryInterface database, ConversionService conversionService) {
        return new UserRepositoryPersistence(database, conversionService);
    }

    @Bean
    public TokenRepositoryPersistence tokenRepositoryPersistence(
            TokenRepositoryInterface database, ConversionService conversionService) {
        return new TokenRepositoryPersistence(database, conversionService);
    }

    @Bean
    public SchedulerRepositoryPersistence schedulerRepositoryPersistence(
            SchedulerRepositoryInterface database, ConversionService conversionService) {
        return new SchedulerRepositoryPersistence(database, conversionService);
    }

    @Bean
    Clock warsawZone() {
        return Clock.system(ZoneId.of("Europe/Warsaw"));
    }
}