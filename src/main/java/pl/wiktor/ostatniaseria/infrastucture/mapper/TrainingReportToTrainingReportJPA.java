package pl.wiktor.ostatniaseria.infrastucture.mapper;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import pl.wiktor.ostatniaseria.domain.report.model.TrainingReport;
import pl.wiktor.ostatniaseria.domain.scheduler.model.Meeting;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.report.entity.MediaJPA;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.report.entity.TrainingReportJPA;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.scheduler.MeetingJPA;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.user.UserJPA;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrainingReportToTrainingReportJPA implements Converter<TrainingReport, TrainingReportJPA> {
    private final ConversionService conversionService;

    public TrainingReportToTrainingReportJPA(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public TrainingReportJPA convert(TrainingReport source) {
        TrainingReportJPA trainingReportJPA = new TrainingReportJPA();
        trainingReportJPA.setDietAndNeatNote(source.dietAndNeatNote());
        trainingReportJPA.setInjuriesNote(source.injuriesNote());
        trainingReportJPA.setProgressNote(source.progressNote());
        trainingReportJPA.setPhotos(toMediaJPA(source.photos()));
        if (source.videos() != null) {
            trainingReportJPA.setVideos(toMediaJPA(source.videos()));
        }
        trainingReportJPA.setUser(conversionService.convert(source.user(), UserJPA.class));
        return trainingReportJPA;
    }

    private List<MediaJPA> toMediaJPA(List<byte[]> bytes) {
        return bytes.stream()
                .map(MediaJPA::new)
                .collect(Collectors.toList());
    }
}
