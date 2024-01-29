package pl.wiktor.ostatniaseria.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.wiktor.ostatniaseria.api.model.request.MeetingRequest;
import pl.wiktor.ostatniaseria.domain.scheduler.SchedulerService;
import pl.wiktor.ostatniaseria.domain.scheduler.model.Meeting;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {
    private final Logger logger = LoggerFactory.getLogger(SchedulerController.class);
    private final SchedulerService service;

    public SchedulerController(SchedulerService service) {
        this.service = service;
    }

    @GetMapping("/available-meetings")
    public List<ZonedDateTime> getAvailableMeetingsTime() {
        logger.info("Getting available meeting times");
        return service.getAvailableMeetingsTime();
    }

    @PostMapping("/book-meeting")
    public Meeting bookMeeting(@RequestBody MeetingRequest request) {
        ZonedDateTime startDateTime = ZonedDateTime.parse(request.start, DateTimeFormatter.ISO_DATE_TIME);
        logger.info("Booking meeting {}", request);
        return service.bookMeeting(startDateTime, request.email);
    }
}
