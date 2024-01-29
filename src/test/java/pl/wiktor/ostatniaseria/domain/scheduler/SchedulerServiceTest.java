package pl.wiktor.ostatniaseria.domain.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchedulerServiceTest {

    SchedulerService service;

    @Mock
    SchedulerRepository repository;

    private Clock clock;
    private LocalTime businessStart;
    private LocalTime businessEnd;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        clock = Clock.systemDefaultZone();
        businessStart = LocalTime.of(9, 0);
        businessEnd = LocalTime.of(18, 0);
        CalendarNotification calendarNotification = (startDate, endDate, title, description) -> {
        };
        service = new SchedulerService(repository, clock, businessStart, businessEnd, calendarNotification);
    }

    @Test
    void getAvailableMeetingsTimeShouldReturnAvailableTimes() {
        List<LocalDateTime> meetings = new ArrayList<>();
        when(repository.getTakenMeetingsBetween(any(Instant.class), any(Instant.class))).thenReturn(meetings);

        List<ZonedDateTime> availableTimes = service.getAvailableMeetingsTime();

        assertNotNull(availableTimes);
        assertNotEquals(0, availableTimes.size());
    }

    @Test
    void getEndOfNextMonthShouldReturnEndOfNextMonth() {
        LocalDate date = LocalDate.now();
        LocalDate expected = date.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        LocalDate actual = service.getEndOfNextMonth(date);
        assertEquals(expected, actual);
    }

    @Test
    void bookMeetingShouldFailBeforeBusinessHours() {
        ZonedDateTime start = ZonedDateTime.of(LocalDate.now(), LocalTime.of(8, 0), ZoneId.systemDefault());
        assertThrows(IllegalArgumentException.class, () -> service.bookMeeting(start, "test@test.com"));
    }

    @Test
    void bookMeetingShouldNotFailAfterBusinessHours() {
        ZonedDateTime start = ZonedDateTime.of(LocalDate.now(), LocalTime.of(17, 45), ZoneId.systemDefault());
        assertDoesNotThrow(() -> service.bookMeeting(start, "test@test.com"));
    }

    @Test
    void bookMeetingShouldFailInvalidMinutesInStart() {
        ZonedDateTime start = ZonedDateTime.of(LocalDate.now(), LocalTime.of(10, 20), ZoneId.systemDefault());
        assertThrows(IllegalArgumentException.class, () -> service.bookMeeting(start, "test@test.com"));
    }

    @Test
    void bookMeetingShouldPassOnValidBusinessHoursAndStart() {
        ZonedDateTime start = ZonedDateTime.of(LocalDate.now(), LocalTime.of(10, 30), ZoneId.systemDefault());
        assertDoesNotThrow(() -> service.bookMeeting(start, "test@test.com"));
    }
}
