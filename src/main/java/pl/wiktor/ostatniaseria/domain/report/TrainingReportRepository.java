package pl.wiktor.ostatniaseria.domain.report;

import pl.wiktor.ostatniaseria.domain.report.model.TrainingReport;

public interface TrainingReportRepository {
    void save(TrainingReport trainingReport);

    byte[] getImage();
}
