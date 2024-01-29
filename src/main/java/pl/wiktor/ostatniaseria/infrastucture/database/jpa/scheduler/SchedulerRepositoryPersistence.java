package pl.wiktor.ostatniaseria.infrastucture.database.jpa.scheduler;

import org.springframework.core.convert.ConversionService;
import pl.wiktor.ostatniaseria.domain.scheduler.SchedulerRepository;
import pl.wiktor.ostatniaseria.domain.scheduler.model.Meeting;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SchedulerRepositoryPersistence implements SchedulerRepository {
    private final SchedulerRepositoryInterface database;
    private final ConversionService conversionService;

    public SchedulerRepositoryPersistence(SchedulerRepositoryInterface database, ConversionService conversionService) {
        this.database = database;
        this.conversionService = conversionService;
    }

    @Override
    public List<LocalDateTime> getTakenMeetingsBetween(Instant start, Instant end) {
        return database.getAllByStartAfterAndFinishBefore(start, end).stream()
                .map(meeting -> meeting.getStart().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .collect(Collectors.toList());
    }

    @Override
    public void save(Meeting meeting) {
        database.save(Objects.requireNonNull(conversionService.convert(meeting, MeetingJPA.class)));
    }
}