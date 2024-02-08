package pl.wiktor.ostatniaseria.api;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.wiktor.ostatniaseria.domain.messages.direct.PersonalMessageService;
import pl.wiktor.ostatniaseria.domain.user.model.User;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessagesController {
    private final PersonalMessageService personalMessageService;

    public MessagesController(PersonalMessageService personalMessageService) {
        this.personalMessageService = personalMessageService;
    }

    @PostMapping("/personal")
    public void sendDirectMessage(@RequestAttribute("user") User from,
                                  @RequestParam("message") String message,
                                  @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        personalMessageService.sendMessage(from, message, files);
    }
}
