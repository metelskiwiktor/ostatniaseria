package pl.wiktor.ostatniaseria.domain.messages.direct;

import pl.wiktor.ostatniaseria.domain.messages.direct.model.PersonalMessage;

public interface PersonalMessageRepository {
    void saveDirectMessage(PersonalMessage personalMessage);
}
