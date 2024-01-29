package pl.wiktor.ostatniaseria.api.model.request;

public class MeetingRequest {
    public String start;
    public String email;

    @Override
    public String toString() {
        return "MeetingRequest{" +
                "start='" + start + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
