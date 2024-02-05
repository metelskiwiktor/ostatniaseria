package pl.wiktor.ostatniaseria.api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static io.restassured.RestAssured.given;

public class UserRegisterTest {
    @BeforeEach
    public void cleanup() {
        try {
            try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ostatniaseria", "postgres", "wiktor");
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("TRUNCATE TABLE training_reportjpa_photos, USERJPA, training_reportjpa CASCADE");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Test
    public void shouldNotAllowDuplicateEmails() {
        String requestBody = "{"
                + "\"username\":\"test_username\","
                + "\"password\":\"test_password1!\","
                + "\"email\":\"duplicateEmail@test.com\""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/register")
                .then()
                .statusCode(201);

//        // Try to register the same user again
        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/register")
                .then()
                .assertThat()
                .statusCode(400); // Assuming the server returns 400 (Bad request) for duplicate emails
    }

    @Test
    public void shouldRequireStrongPassword() {
        String weakPasswordRequest = "{"
                + "\"username\":\"test_username\","
                + "\"password\":\"weak\","
                + "\"email\":\"strongPassword@test.com\""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(weakPasswordRequest)
                .when()
                .post("/register")
                .then()
                .assertThat()
                .statusCode(400); // Assuming the server returns 400 (Bad request) for weak password
    }

    @Test
    public void shouldRequireCorrectEmailSyntax() {
        String incorrectSyntaxEmail = "{"
                + "\"username\":\"test_username\","
                + "\"password\":\"validPassword1!\","
                + "\"email\":\"incorrectSyntaxEmail\""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(incorrectSyntaxEmail)
                .when()
                .post("/register")
                .then()
                .assertThat()
                .statusCode(400); // Assuming the server returns 400 (Bad request) for incorrect email syntax
    }
}