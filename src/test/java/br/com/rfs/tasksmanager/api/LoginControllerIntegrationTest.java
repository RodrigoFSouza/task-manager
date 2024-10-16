package br.com.rfs.tasksmanager.api;

import br.com.rfs.tasksmanager.domain.dto.request.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.core.IsNull.notNullValue;


public class LoginControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    public void testLogin_Success() {
        LoginRequest loginRequest = new LoginRequest("admin", "123");

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(loginRequest)
            .when()
            .post("/v1/login")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("accessToken", notNullValue())
            .body("expiresIn", notNullValue());
    }

    @Test
    public void testLogin_InvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest("admin", "invalid-password");

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(loginRequest)
            .when()
            .post("/v1/login")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void testLogin_MissingFields() {
        LoginRequest loginRequest = new LoginRequest("", "");

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(loginRequest)
            .when()
            .post("/v1/login")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}