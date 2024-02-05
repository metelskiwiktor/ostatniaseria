package pl.wiktor.ostatniaseria.domain.report;

import org.springframework.web.multipart.MultipartFile;
import pl.wiktor.ostatniaseria.domain.report.model.TrainingReport;
import pl.wiktor.ostatniaseria.domain.user.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrainingReportService {
    private final TrainingReportRepository trainingReportRepository;

    public TrainingReportService(TrainingReportRepository trainingReportRepository) {
        this.trainingReportRepository = trainingReportRepository;
    }

    public void report(String progressNote, String dietAndNeatNote, String injuriesNote,
                       List<MultipartFile> photos, List<MultipartFile> videos, User user) throws IOException {
        List<byte[]> photosBytes = new ArrayList<>();
        List<byte[]> videosBytes = new ArrayList<>();

        for (MultipartFile photo : photos) {
            photosBytes.add(photo.getBytes());
        }

        if (videos != null) {
            for (MultipartFile video : videos) {
                videosBytes.add(video.getBytes());
            }
        }


        TrainingReport trainingReport = new TrainingReport(
                progressNote,
                dietAndNeatNote,
                injuriesNote,
                photosBytes,
                videosBytes,
                user
        );

        trainingReportRepository.save(trainingReport);
    }

    public byte[] getImage() {
        return trainingReportRepository.getImage();
    }
}
