package pl.wiktor.ostatniaseria.infrastucture.database.jpa.report;

import org.springframework.core.convert.ConversionService;
import org.springframework.transaction.annotation.Transactional;
import pl.wiktor.ostatniaseria.domain.report.TrainingReportRepository;
import pl.wiktor.ostatniaseria.domain.report.model.TrainingReport;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.report.entity.MediaJPA;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.report.entity.TrainingReportJPA;

import java.util.List;
import java.util.Objects;

public class TrainingReportRepositoryPersistence implements TrainingReportRepository {
    private final TrainingReportRepositoryInterface database;
    private final ConversionService conversionService;

    public TrainingReportRepositoryPersistence(TrainingReportRepositoryInterface database, ConversionService conversionService) {
        this.database = database;
        this.conversionService = conversionService;
    }

    @Transactional
    @Override
    public void save(TrainingReport trainingReport) {
        TrainingReportJPA reportJPA = conversionService.convert(trainingReport, TrainingReportJPA.class);
        database.save(Objects.requireNonNull(reportJPA));
    }

    @Override
    public byte[] getImage() {
        List<TrainingReportJPA> all = database.findAll();
        //get first non-empty image
        for (TrainingReportJPA report : all) {
            if (report.getPhotos() != null && !report.getPhotos().isEmpty()) {
                for (MediaJPA media : report.getPhotos()) {
                    if (media != null && media.getData() != null && media.getData().length > 0) {
                        return media.getData();
                    }
                }
            }
        }
        return null;
    }
}
