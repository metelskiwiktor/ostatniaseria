package pl.wiktor.ostatniaseria.infrastucture.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import pl.wiktor.ostatniaseria.domain.scheduler.CalendarNotification;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalendarGoogle implements CalendarNotification {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarGoogle.class);
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "google-credentials.json";
    private static final String CALENDAR_ID = "primary";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String USER_KEY = "user";
    private static final String APPLICATION_NAME = "ostatniaseria";
    private final Calendar calendar;
    private final ExecutorService executorService = Executors.newFixedThreadPool(15);

    public CalendarGoogle() throws IOException, GeneralSecurityException {
        this.calendar = initializeCalendarService();
    }

    @Override
    public void schedule(ZonedDateTime startDate, ZonedDateTime endDate, String title, String description) {
        //log all info
        LOGGER.info("Scheduling calendar event: Start Date - {}, End Date - {}, Title - {}, Description - {}", startDate, endDate, title, description);
        executorService.execute(() -> {
            try {
                // Integrate with Google Calendar
                String startDateTime = startDate.format(DateTimeFormatter.ISO_DATE_TIME);
                String endDateTime = endDate.format(DateTimeFormatter.ISO_DATE_TIME);
                createEventInGoogleCalendar(startDateTime, endDateTime, title, description);
            } catch (IOException | GeneralSecurityException e) {
                LOGGER.error("Error during Google Calendar integration: {}", e.getMessage(), e);
            }
        });
    }

    private void createEventInGoogleCalendar(String startDateTime, String endDateTime, String summary, String description) throws IOException, GeneralSecurityException {
        Event event = createEvent(startDateTime, endDateTime, summary, description);

        // Add the event to the primary calendar
        event = calendar.events().insert(CALENDAR_ID, event).execute();
        //log event is created
        LOGGER.info("Event created: {}", event.getSummary());
    }

    private Event createEvent(String startDateTime, String endDateTime, String summary, String description) {
        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        DateTime start = new DateTime(startDateTime);
        event.setStart(new EventDateTime().setDateTime(start));
        DateTime end = new DateTime(endDateTime);
        event.setEnd(new EventDateTime().setDateTime(end));

        return event;
    }

    private Calendar initializeCalendarService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        try (InputStream in = new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream()) {
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
            GoogleAuthorizationCodeFlow flow =
                    new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                            .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                            .setAccessType("offline")
                            .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8889).build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize(USER_KEY);
            return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
        }
    }
}