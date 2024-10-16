package br.com.rfs.tasksmanager.util;

import br.com.rfs.tasksmanager.domain.dto.request.LoginRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

public class JwtTokenUtil {

    private static String token;

    public static String generateJwtToken(int port) {
        if (token == null) {
            LoginRequest loginRequest = new LoginRequest("admin", "123");

            token = RestAssured
                .given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(loginRequest)
                .when()
                    .post("/v1/login")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .path("accessToken");
        }

        return token;
    }

    public static void resetToken() {
        token = null;
    }
}