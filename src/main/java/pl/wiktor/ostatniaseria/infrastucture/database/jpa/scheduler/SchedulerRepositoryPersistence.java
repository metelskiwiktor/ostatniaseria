package pl.wiktor.ostatniaseria.infrastucture.database.jpa.scheduler;

import org.springframework.core.convert.ConversionService;
import pl.wiktor.ostatniaseria.domain.scheduler.SchedulerRepository;
import pl.wiktor.ostatniaseria.domain.scheduler.model.Meeting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SchedulerRepositoryPersistence implements SchedulerRepository {
    private final SchedulerRepositoryInterface database;
    private final ConversionService conversionService;

    public SchedulerRepositoryPersistence(SchedulerRepositoryInterface database, ConversionService conversionService) {
        this.database = database;
        this.conversionService = conversionService;
    }

    @Override
    public List<LocalDateTime> getTakenMeetingsBetween(LocalDate start, LocalDate end) {
        return database.getAllByStartAfterAndEndBefore();
    }

    @Override
    public void save(Meeting meeting) {

    }
}
