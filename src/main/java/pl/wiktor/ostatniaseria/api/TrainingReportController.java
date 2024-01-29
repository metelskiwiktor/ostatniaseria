package pl.wiktor.ostatniaseria.api;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.wiktor.ostatniaseria.api.security.RequiresLogin;
import pl.wiktor.ostatniaseria.domain.report.TrainingReportService;
import pl.wiktor.ostatniaseria.domain.user.model.register.User;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/training")
public class TrainingReportController {

    private final TrainingReportService trainingReportService;

    public TrainingReportController(TrainingReportService trainingReportService) {
        this.trainingReportService = trainingReportService;
    }

    @RequiresLogin
    @PostMapping("/report")
    public void save(@RequestParam("injuriesNote") String injuriesNote,
                     @RequestParam("progressNote") String progressNote,
                     @RequestParam("dietAndNeatNote") String dietAndNeatNote,
                     @RequestParam("photos") List<MultipartFile> photos,
                     @RequestParam(value = "videos", required = false) List<MultipartFile> videos,
                     @RequestAttribute("user") User user
    ) throws IOException {
        trainingReportService.report(
                progressNote,
                dietAndNeatNote,
                injuriesNote,
                photos,
                videos,
                user
        );
    }

    @GetMapping(value = "/images", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<ByteArrayResource> getImage() {
        byte[] image = trainingReportService.getImage();
        final ByteArrayResource resource = new ByteArrayResource(image);

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}