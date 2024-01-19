package pl.wiktor.ostatniaseria.infrastucture.database.jpa.scheduler;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wiktor.ostatniaseria.domain.scheduler.model.Meeting;

import java.time.Instant;
import java.util.List;

public interface SchedulerRepositoryInterface extends JpaRepository<MeetingJPA, String> {
    List<Meeting> getAllByStartAfterAndEndBefore(Instant start, Instant end);
}
