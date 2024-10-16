package br.com.rfs.tasksmanager.api;

import br.com.rfs.tasksmanager.repositories.UserRepository;
import br.com.rfs.tasksmanager.util.DatabaseCleaner;
import br.com.rfs.tasksmanager.util.JwtTokenUtil;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    DatabaseCleaner cleaner;

    static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:8.0.34");
        MY_SQL_CONTAINER.start();
    }

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
    }

    protected String getJwtToken() {
        return JwtTokenUtil.generateJwtToken(port);
    }

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",() -> MY_SQL_CONTAINER.getJdbcUrl());
        registry.add("spring.datasource.username",() -> MY_SQL_CONTAINER.getUsername());
        registry.add("spring.datasource.password",() -> MY_SQL_CONTAINER.getPassword());
    }

    protected void cleanTables(String ... tables) {
        for(String table: tables) {
            cleaner.clean(table);
        }
    }
}