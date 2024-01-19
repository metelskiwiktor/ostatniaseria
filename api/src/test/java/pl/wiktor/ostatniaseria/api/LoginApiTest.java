package pl.wiktor.ostatniaseria.api;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anything;

public class LoginApiTest {
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
                stmt.executeUpdate("TRUNCATE TABLE TOKENJPA RESTART IDENTITY");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void shouldReturnTokenOnValidCredentials() {
        // Register user
        String registerBody = "{"
                + "\"email\":\"valid.email@test.com\","
                + "\"password\":\"validPassword1!\""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(registerBody)
                .when()
                .post("/register")
                .then()
                .statusCode(201);

        // Login user
        String loginBody = "{"
                + "\"email\":\"valid.email@test.com\","
                + "\"password\":\"validPassword1!\""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .contentType(ContentType.TEXT)
                .body(anything());
    }

    @Test
    void shouldGiveErrorOnInvalidCredentials() {
        // Register user
        String registerBody = "{"
                + "\"email\":\"valid.email@test.com\","
                + "\"password\":\"validPassword1!\""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(registerBody)
                .when()
                .post("/register")
                .then()
                .statusCode(201);

        // Attempt to login with invalid password
        String loginBodyInvalidPassword = "{"
                + "\"email\":\"valid.email@test.com\","
                + "\"password\":\"invalidPassword1!\""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(loginBodyInvalidPassword)
                .when()
                .post("/login")
                .then()
                .statusCode(404);

        // Attempt to login with non-existent user
        String loginBodyNonExistentUser = "{"
                + "\"email\":\"nonexistent.email@test.com\","
                + "\"password\":\"anyPassword1!\""
                + "}";

        given()
                .contentType(ContentType.JSON)
                .body(loginBodyNonExistentUser)
                .when()
                .post("/login")
                .then()
                .statusCode(404);
    }
}