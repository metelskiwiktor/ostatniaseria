package pl.wiktor.ostatniaseria.infrastucture.database.jpa.report.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MediaJPA {
    @Column(columnDefinition = "bytea")
    private byte[] data;

    public MediaJPA() {
    }

    public MediaJPA(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
