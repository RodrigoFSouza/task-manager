package br.com.rfs.tasksmanager.api;

import br.com.rfs.tasksmanager.domain.constant.TodoStatus;
import br.com.rfs.tasksmanager.domain.dto.request.CreateTodoRequest;
import br.com.rfs.tasksmanager.domain.dto.request.UpdateTodoRequest;
import br.com.rfs.tasksmanager.domain.entity.Todo;
import br.com.rfs.tasksmanager.repositories.TodoRepository;
import br.com.rfs.tasksmanager.util.JwtTokenUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.hamcrest.core.IsEqual.equalTo;

public class TodoControllerIntegrationTest extends BaseIntegrationTest {

    private static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    @Autowired
    private TodoRepository todoRepository;
    private Long todoId;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        super.cleanTables("todo");
        createTodoEntity("New Task 1", "Description", TodoStatus.IN_PROGRESS);
        createTodoEntity("New Task 2", "Description", TodoStatus.IN_PROGRESS);
        createTodoEntity("New Task 3", "Description", TodoStatus.COMPLETED);
        createTodoEntity("New Task 4", "Description", TodoStatus.COMPLETED);
        Todo todoNew = createTodoEntity("New Task 5", "Description", TodoStatus.NEW);
        todoId = todoNew.getId();
    }

    @Test
    @Order(1)
    public void testCreateTodo_Success() {
        String token = JwtTokenUtil.generateJwtToken(port);

        CreateTodoRequest request = new CreateTodoRequest("New Task", "Description");

        RestAssured
            .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
            .when()
                .post("/v1/todos")
            .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @Order(2)
    public void testListAllTodos_Success() {
        String token = JwtTokenUtil.generateJwtToken(port);

        RestAssured
            .given()
                .header("Authorization", "Bearer " + token)
                .queryParam("page", 0)
                .queryParam("size", 10)
            .when()
                .get("/v1/todos")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("page", equalTo(0))
                .body("size", equalTo(10))
                .body("totalPages", equalTo(1))
                .body("totalElements", equalTo(5));
    }

    @Test
    @Order(3)
    public void testListAllTodos_SuccessByStatus() {
        String token = JwtTokenUtil.generateJwtToken(port);

        RestAssured
            .given()
                .header("Authorization", "Bearer " + token)
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("status", STATUS_IN_PROGRESS)
            .when()
                .get("/v1/todos")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("page", equalTo(0))
                .body("size", equalTo(10))
                .body("totalPages", equalTo(1))
                .body("totalElements", equalTo(2));
    }



    @Test
    @Order(4)
    public void testListTodos_Unauthorized() {
        RestAssured
            .given()
                .queryParam("page", 0)
                .queryParam("size", 10)
            .when()
                .get("/v1/todos")
            .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @Order(5)
    public void testCreateTodo_InvalidRequest() {
        String token = JwtTokenUtil.generateJwtToken(port);

        CreateTodoRequest request = new CreateTodoRequest("", "Description");

        RestAssured
            .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
            .when()
                .post("/v1/todos")
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(6)
    public void testUpdateTodo_Success() {
        String token = getJwtToken();

        UpdateTodoRequest request = new UpdateTodoRequest("Updated Task", "completed", STATUS_IN_PROGRESS);

        RestAssured
            .given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
            .when()
                .put("/v1/todos/" + todoId)
            .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(7)
    public void testDeleteTodo_Success() {
        String token = getJwtToken();

        RestAssured
            .given()
                .header("Authorization", "Bearer " + token)
            .when()
                .delete("/v1/todos/" + todoId)
            .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(8)
    public void testDeleteTodo_Unauthorized() {
        RestAssured
            .given()
            .when()
                .delete("/v1/todos/1")
            .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    private Todo createTodoEntity(String title, String description, TodoStatus statusInProgress) {
        var newTodo = new Todo();
        newTodo.setTitle(title);
        newTodo.setDescription(description);
        newTodo.setStatus(statusInProgress);
        return todoRepository.save(newTodo);
    }
}