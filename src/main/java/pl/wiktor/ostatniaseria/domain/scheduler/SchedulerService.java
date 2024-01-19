package pl.wiktor.ostatniaseria.domain.scheduler;

import pl.wiktor.ostatniaseria.domain.scheduler.model.Meeting;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class SchedulerService {
    private final SchedulerRepository schedulerRepository;
    private final Clock clock;
    private final LocalTime businessStartHour;
    private final LocalTime businessEndHour;

    public SchedulerService(SchedulerRepository schedulerRepository, Clock clock, LocalTime businessStartHour, LocalTime businessEndHour) {
        this.schedulerRepository = schedulerRepository;
        this.clock = clock;
        this.businessStartHour = businessStartHour;
        this.businessEndHour = businessEndHour;
    }

    public List<LocalDateTime> getAvailableMeetingsTime() {
        LocalDate today = LocalDate.now(clock);
        LocalDate endOfNextMonth = getEndOfNextMonth(today);
        Set<LocalDateTime> takenMeetings = new HashSet<>(schedulerRepository.getTakenMeetingsBetween(today, endOfNextMonth));
        List<LocalDateTime> allMeetings = new ArrayList<>();
        LocalDateTime start = today.atTime(businessStartHour);
        while (!start.isAfter(endOfNextMonth.atStartOfDay())) {
            if (isWorkingDay(start.toLocalDate()) &&
                    start.toLocalTime().isBefore(businessEndHour) &&
                    start.toLocalTime().isAfter(businessStartHour) ||
                    start.toLocalTime().equals(businessStartHour)) {
                allMeetings.add(start);
            }
            start = start.plusMinutes(45);
            if (start.toLocalTime().isAfter(businessEndHour)) {
                start = start.toLocalDate().plusDays(1).atStartOfDay().plusHours(businessStartHour.getHour());
            }
        }
        return allMeetings.stream()
                .filter(meeting -> !takenMeetings.contains(meeting))
                .collect(Collectors.toList());
    }

    private boolean isWorkingDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return !(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
    }

    public Meeting bookMeeting(ZonedDateTime start, String email) {
        ZonedDateTime end = start.plusMinutes(45);
        checkWithinBusinessHours(start, end);
        checkMeetingStartTime(start);
        Meeting meeting = new Meeting(UUID.randomUUID().toString(), email, start, end);
        schedulerRepository.save(meeting);
        return meeting;
    }

    private void checkWithinBusinessHours(ZonedDateTime start, ZonedDateTime end) {
        int startOfDayInMinutes = toMinutes(start);
        int endOfDayInMinutes = toMinutes(end);

        if (!isWithinBusinessHours(startOfDayInMinutes) || !isWithinBusinessHours(endOfDayInMinutes)) {
            throw new IllegalArgumentException(
                    "The meeting should be booked within business hours ("
                            + businessStartHour.getHour() + " - "
                            + businessEndHour.getHour() + ")");
        }
    }

    private int toMinutes(ZonedDateTime time) {
        return time.getHour() * 60 + time.getMinute();
    }

    private boolean isWithinBusinessHours(int timeInMinutes) {
        int startDayInMinutes = businessStartHour.getHour() * 60 + businessStartHour.getMinute();
        int endDayInMinutes = businessEndHour.getHour() * 60 + businessEndHour.getMinute();

        return timeInMinutes >= startDayInMinutes && timeInMinutes <= endDayInMinutes;
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