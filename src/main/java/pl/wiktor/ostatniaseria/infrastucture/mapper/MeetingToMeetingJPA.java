package pl.wiktor.ostatniaseria.infrastucture.mapper;

import org.springframework.core.convert.converter.Converter;
import pl.wiktor.ostatniaseria.domain.scheduler.model.Meeting;
import pl.wiktor.ostatniaseria.infrastucture.database.jpa.scheduler.MeetingJPA;

public class MeetingToMeetingJPA implements Converter<Meeting, MeetingJPA> {
    @Override
    public MeetingJPA convert(Meeting source) {
        MeetingJPA meetingJPA = new MeetingJPA();
        meetingJPA.setMeetingId(source.meetingId());
        meetingJPA.setEmail(meetingJPA.getEmail());
        meetingJPA.setStart(meetingJPA.getStart());
        meetingJPA.setEnd(meetingJPA.getEnd());
        return meetingJPA;
    }
}
