package br.com.rfs.tasksmanager.api;

import br.com.rfs.tasksmanager.domain.dto.request.CreateUserRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.core.IsEqual.equalTo;

public class UserControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    public void testCreateUser_Success() {
        String token = getJwtToken();
        CreateUserRequest request = new CreateUserRequest("testuser", "testuser@example.com");

        RestAssured
            .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
            .when()
                .post("/v1/users")
            .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void testCreateUser_InvalidRequest() {
        String token = getJwtToken();
        CreateUserRequest request = new CreateUserRequest("", "");

        RestAssured
            .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
            .when()
                .post("/v1/users")
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"SCOPE_ADMIN"})
    public void testListUsers_Success() {
        String token = getJwtToken();
        RestAssured
                .given()
                    .header("Authorization", "Bearer " + token)
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                .when()
                    .get("/v1/users")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page", equalTo(0))
                    .body("size", equalTo(10))
                    .body("totalPages", equalTo(1))
                    .body("totalElements", equalTo(1));
    }

    @Test
    public void testListUsers_Unauthorized() {
        RestAssured
                .given()
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                .when()
                    .get("/v1/users")
                .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}