package containers.postgresql;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import repository.impl.UserRepositoryImpl;
import services.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PostgreSQL {

    private static final String IMAGE_NAME = "postgres:13-alpine";
    private static final int PORT = 5432;
    private static final String DATABASE_NAME = "calendar";
    private static final String DATABASE_USERNAME = "psqluser";
    private static final String DATABASE_PASSWORD = "psqlpassword";

    private static int HOST_PORT;

    private static final Logger logger = Logger.getLogger(PostgreSQL.class.getName());

    private static UserService userService;

    @Container
    private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(IMAGE_NAME)
            .withDatabaseName(DATABASE_NAME).withUsername(DATABASE_USERNAME).withPassword(DATABASE_PASSWORD).withExposedPorts(PORT);

    @BeforeAll
    public static void setup() {
        postgresqlContainer.start();
        HOST_PORT = postgresqlContainer.getMappedPort(PORT);
        logger.info("Started container " + postgresqlContainer.getDockerImageName() + " on port " + HOST_PORT);
    }

    @BeforeEach
    public void initHibernate() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("psql", getPsqlPersistenceUnit(HOST_PORT));
        logger.info("@@@ Properties: " + entityManagerFactory.getProperties());
        UserRepositoryImpl userRepository = new UserRepositoryImpl(entityManagerFactory);
        userService = new UserService(userRepository);
    }

    private Map<String, String> getPsqlPersistenceUnit(int port) {
        return new HashMap<>() {{
            put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            put("hibernate.connection.driver_class", "org.postgresql.Driver");
            put("hibernate.connection.url", String.format("jdbc:postgresql://localhost:%s/%s", port, DATABASE_NAME));
            put("hibernate.connection.username", DATABASE_USERNAME);
            put("hibernate.connection.password", DATABASE_PASSWORD);
            put("hibernate.show_sql", "true");
            put("hibernate.hbm2ddl.auto", "create-drop");
            put("hibernate.current_session_context_class",
                    "org.hibernate.context.internal.ThreadLocalSessionContext");
        }};

    }

    @AfterAll
    public static void teardown() {
        postgresqlContainer.stop();
        logger.info("Stopped container " + postgresqlContainer.getDockerImageName());
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(postgresqlContainer.getJdbcUrl(), postgresqlContainer.getUsername(),
                postgresqlContainer.getPassword());
        Assertions.assertEquals("calendar", connection.getCatalog());
    }

    @Test
    public void testCreateUser() {
        // Arrange
        User newUser = new User("johndoe");
        int initialSize = userService.findAll().size();

        // Act
        userService.createUser(newUser);
        List<User> users = userService.findAll();

        // Assert
        Assertions.assertEquals(initialSize + 1, users.size());
        Assertions.assertEquals(newUser, users.get(users.size() - 1));
    }

    @Test
    public void testEmptyDatabase() {
        Assertions.assertEquals(0, userService.findAll().size());
    }
}
