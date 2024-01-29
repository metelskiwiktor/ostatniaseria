package pl.wiktor.ostatniaseria.domain.scheduler;

import pl.wiktor.ostatniaseria.domain.scheduler.model.Meeting;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public interface SchedulerRepository {
    List<LocalDateTime> getTakenMeetingsBetween(Instant start, Instant end);

    void save(Meeting meeting);
}
