package pl.wiktor.ostatniaseria.domain.report.model;

import pl.wiktor.ostatniaseria.domain.user.model.register.User;

import java.util.List;

public record TrainingReport(String progressNote, String dietAndNeatNote, String injuriesNote,
                             List<byte[]> photos, List<byte[]> videos, User user) {
}
