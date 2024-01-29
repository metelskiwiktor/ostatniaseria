package pl.wiktor.ostatniaseria.api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MeetingsApiTest {
    @Test
    public void shouldBookMeetingAndNotSeeItInAvailableListAgain() {
        // Get the first available meeting
        List<String> availableMeetingsAsStrings = given()
                .contentType(ContentType.JSON)
                .when()
                .get("scheduler/available-meetings")
                .then()
                .statusCode(200)
                .body("$", not(empty())) // Assuming the list is not empty
                .extract()
                .jsonPath().getList("$", String.class);

        String firstAvailableMeeting = availableMeetingsAsStrings.get(0);

        // Book this meeting
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("start", firstAvailableMeeting, "email", "test@example.com"))
                .when()
                .post("scheduler/book-meeting")
                .then()
                .statusCode(200);

        // Get the available meetings again
        List<String> availableMeetingsAsStringsAfterBooking = given()
                .contentType(ContentType.JSON)
                .when()
                .get("scheduler/available-meetings")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList("$", String.class);

        // Check that the booked meeting is no longer in the available list
        assertThat(availableMeetingsAsStringsAfterBooking, not(hasItem(firstAvailableMeeting)));
    }
}