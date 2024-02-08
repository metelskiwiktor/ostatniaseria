package pl.wiktor.ostatniaseria.domain.messages.direct;

import org.springframework.web.multipart.MultipartFile;
import pl.wiktor.ostatniaseria.domain.messages.direct.model.PersonalMessage;
import pl.wiktor.ostatniaseria.domain.model.Media;
import pl.wiktor.ostatniaseria.domain.user.UserService;
import pl.wiktor.ostatniaseria.domain.user.model.User;

import java.io.IOException;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class PersonalMessageService {
    private final Clock clock;
    private final UserService userService;
    private final PersonalMessageRepository personalMessageRepository;

    public PersonalMessageService(Clock clock, UserService userService, PersonalMessageRepository personalMessageRepository) {
        this.clock = clock;
        this.userService = userService;
        this.personalMessageRepository = personalMessageRepository;
    }

    public void sendMessage(User from, String message, List<MultipartFile> files) throws IOException {
        List<byte[]> fileBytes = new ArrayList<>();

        if (files != null) {
            for (MultipartFile file : files) {
                fileBytes.add(file.getBytes());
            }
        }

        PersonalMessage personalMessage = new PersonalMessage(
                from,
                ZonedDateTime.now(clock),
                message,
                new Media(fileBytes)
        );

        personalMessageRepository.saveDirectMessage(personalMessage);
    }
}
