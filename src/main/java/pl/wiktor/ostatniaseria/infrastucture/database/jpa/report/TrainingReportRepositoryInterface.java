package pl.wiktor.ostatniaseria.infrastucture.database.jpa.report;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.report.entity.TrainingReportJPA;

public interface TrainingReportRepositoryInterface extends JpaRepository<TrainingReportJPA, String> {
}
