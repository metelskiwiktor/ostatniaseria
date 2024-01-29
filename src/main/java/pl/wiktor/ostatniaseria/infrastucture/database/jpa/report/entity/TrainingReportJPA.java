package pl.wiktor.ostatniaseria.infrastucture.database.jpa.report.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.user.UserJPA;

import java.util.List;

@Entity
public class TrainingReportJPA {
    @Id
    @UuidGenerator
    private String id;
    private String injuriesNote;
    private String progressNote;
    private String dietAndNeatNote;
    @ElementCollection
    private List<MediaJPA> photos;
    @ElementCollection
    private List<MediaJPA> videos;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserJPA user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInjuriesNote() {
        return injuriesNote;
    }

    public void setInjuriesNote(String injuriesNote) {
        this.injuriesNote = injuriesNote;
    }

    public String getProgressNote() {
        return progressNote;
    }

    public void setProgressNote(String progressNote) {
        this.progressNote = progressNote;
    }

    public String getDietAndNeatNote() {
        return dietAndNeatNote;
    }

    public void setDietAndNeatNote(String dietAndMovementNote) {
        this.dietAndNeatNote = dietAndMovementNote;
    }

    public List<MediaJPA> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MediaJPA> photos) {
        this.photos = photos;
    }

    public List<MediaJPA> getVideos() {
        return videos;
    }

    public void setVideos(List<MediaJPA> videos) {
        this.videos = videos;
    }

    public UserJPA getUser() {
        return user;
    }

    public void setUser(UserJPA user) {
        this.user = user;
    }
}