package pl.wiktor.ostatniaseria.domain.scheduler;

import java.time.ZonedDateTime;

public interface CalendarNotification {
    void schedule(ZonedDateTime startDate, ZonedDateTime endDate, String title, String description);
}
