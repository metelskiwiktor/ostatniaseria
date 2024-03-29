package pl.wiktor.ostatniaseria.domain.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.wiktor.ostatniaseria.domain.scheduler.model.Meeting;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class SchedulerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerService.class);
    private final SchedulerRepository schedulerRepository;
    private final Clock clock;
    private final LocalTime businessStartHour;
    private final LocalTime businessEndHour;
    private final CalendarNotification calendarNotification;

    public SchedulerService(SchedulerRepository schedulerRepository, Clock clock, LocalTime businessStartHour,
                            LocalTime businessEndHour, CalendarNotification calendarNotification) {
        this.schedulerRepository = schedulerRepository;
        this.clock = clock;
        this.businessStartHour = businessStartHour;
        this.businessEndHour = businessEndHour;
        this.calendarNotification = calendarNotification;
    }

    public List<ZonedDateTime> getAvailableMeetingsTime() {
        LocalDate today = LocalDate.now(clock);
        LocalDate endOfNextMonth = getEndOfNextMonth(today);
        List<LocalDateTime> takenMeetings = schedulerRepository.getTakenMeetingsBetween(today.atStartOfDay(clock.getZone()).toInstant(),
                endOfNextMonth.atStartOfDay(clock.getZone()).toInstant());
        LOGGER.info("Taken meetings: {}", takenMeetings);
        // null check
        if (takenMeetings == null) {
            takenMeetings = new ArrayList<>();
        }

        Set<LocalDateTime> takenMeetingsSet = new HashSet<>(takenMeetings);
        List<LocalDateTime> allMeetings = new ArrayList<>();
        LocalDateTime start = today.atTime(businessStartHour);

        while (!start.isAfter(endOfNextMonth.atStartOfDay())) {
            start = start.plusMinutes(45);
            if (isWorkingDay(start.toLocalDate()) &&
                    !start.toLocalTime().isBefore(businessStartHour) &&
                    !start.toLocalTime().isAfter(businessEndHour)) {
                allMeetings.add(start);
            }

            if (start.toLocalTime().isAfter(businessEndHour)) {
                start = start.toLocalDate().plusDays(1).atStartOfDay().plusHours(businessStartHour.getHour());
            }
        }

        return allMeetings.stream()
                .filter(meeting -> !takenMeetingsSet.contains(meeting))
                .map(localDateTime -> ZonedDateTime.of(localDateTime, clock.getZone()))
                .collect(Collectors.toList());
    }

    private boolean isWorkingDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return !(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
    }

    public Meeting bookMeeting(ZonedDateTime start, String email) {
        ZonedDateTime end = start.plusMinutes(45);
        checkWithinBusinessHours(start);
        checkMeetingStartTime(start);
        Meeting meeting = new Meeting(UUID.randomUUID().toString(), email, start, end);
        schedulerRepository.save(meeting);
        LOGGER.info("Meeting booked: {}", meeting);
        calendarNotification.schedule(start, end, "Meeting with " + email, "Meeting description");
        return meeting;
    }

    private void checkWithinBusinessHours(ZonedDateTime start) {
        int startOfDayInMinutes = start.getHour() * 60 + start.getMinute();

        if (isNotWithinBusinessHours(startOfDayInMinutes)) {
            throw new IllegalArgumentException(
                    "The meeting should be booked within business hours ("
                            + businessStartHour.getHour() + " - "
                            + businessEndHour.getHour() + ")");
        }
    }

    private boolean isNotWithinBusinessHours(int timeInMinutes) {
        int startDayInMinutes = businessStartHour.getHour() * 60 + businessStartHour.getMinute();
        int endDayInMinutes = businessEndHour.getHour() * 60 + businessEndHour.getMinute();

        return timeInMinutes < startDayInMinutes || timeInMinutes > endDayInMinutes;
    }

    private void checkMeetingStartTime(ZonedDateTime start) {
        long minutesFromStartOfDay = ChronoUnit.MINUTES.between(start.with(LocalTime.MIN), start);
        if (minutesFromStartOfDay % 15 != 0) {
            throw new IllegalArgumentException(
                    "Invalid meeting start time. Meeting can only start at x:00, x:15, x:30 or x:45 where x is an hour within "
                            + businessStartHour.getHour() + " and "
                            + businessEndHour.getHour());
        }
    }

    LocalDate getEndOfNextMonth(LocalDate date) {
        YearMonth nextMonth = YearMonth.of(date.getYear(), date.getMonth().plus(1));
        return nextMonth.atEndOfMonth();
    }
}
