package pl.wiktor.ostatniaseria.domain.scheduler;

import pl.wiktor.ostatniaseria.domain.scheduler.model.Meeting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SchedulerRepository {
    List<LocalDateTime> getTakenMeetingsBetween(LocalDate start, LocalDate end);

    void save(Meeting meeting);
}
