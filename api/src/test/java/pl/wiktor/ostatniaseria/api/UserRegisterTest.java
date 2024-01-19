package pl.wiktor.ostatniaseria.api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static io.restassured.RestAssured.given;

public class UserRegisterTest {
    @BeforeEach
    public void cleanup() {
        try {
            // Set the PostgreSQL Driver
            Class.forName("org.postgresql.Driver");

            // Set PostgreSQL database details
            try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ostatniaseria", "postgres", "wiktor");
                 Statement stmt = conn.createStatement()) {

                // Truncate PostgreSQL Table
                stmt.executeUpdate("TRUNCATE TABLE USERJPA RESTART IDENTITY");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldNotAllowDuplicateEmails() {
        String requestBody = "{"
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