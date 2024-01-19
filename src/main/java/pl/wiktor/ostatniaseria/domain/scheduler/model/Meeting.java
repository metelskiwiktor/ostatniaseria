package pl.wiktor.ostatniaseria.domain.scheduler.model;

import java.time.ZonedDateTime;

public record Meeting(String meetingId, String email, ZonedDateTime start, ZonedDateTime end) {
}
