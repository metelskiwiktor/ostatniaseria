package pl.wiktor.ostatniaseria.infrastucture.database.jpa.scheduler;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface SchedulerRepositoryInterface extends JpaRepository<MeetingJPA, String> {
    List<MeetingJPA> getAllByStartAfterAndFinishBefore(Instant start, Instant finish);
}
