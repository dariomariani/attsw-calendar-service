package containers.mysql;

import containers.postgresql.PostgreSQL;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import models.User;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;
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

public class MySQL {

    private static final String IMAGE_NAME = "mysql:8.0";
    private static final int PORT = 3306;
    private static final String DATABASE_NAME = "calendar";
    private static final String DATABASE_USERNAME = "mysqluser";
    private static final String DATABASE_PASSWORD = "mysqlpassword";

    private static int HOST_PORT;

    private static final Logger logger = Logger.getLogger(PostgreSQL.class.getName());

    private static UserService userService;

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>(IMAGE_NAME).withDatabaseName(DATABASE_NAME)
            .withUsername(DATABASE_USERNAME).withPassword(DATABASE_PASSWORD).withEnv("MYSQL_ROOT_PASSWORD", "rootpassword").withExposedPorts(PORT);

    @BeforeAll
    public static void setup() {
        mysqlContainer.start();
        HOST_PORT = mysqlContainer.getMappedPort(PORT);
        logger.info("Started container " + mysqlContainer.getDockerImageName() + " on port " + HOST_PORT);
    }

    @BeforeEach
    public void initHibernate() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("mysql", getPsqlPersistenceUnit(HOST_PORT));
        logger.info("@@@ Properties: " + entityManagerFactory.getProperties());
        UserRepositoryImpl userRepository = new UserRepositoryImpl(entityManagerFactory);
        userService = new UserService(userRepository);
    }

    private Map<String, String> getPsqlPersistenceUnit(int port) {
        return new HashMap<>() {{
            put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
            put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            put("hibernate.connection.url", String.format("jdbc:mysql://localhost:%s/%s", port, DATABASE_NAME));
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
        mysqlContainer.stop();
        logger.info("Stopped container " + mysqlContainer.getDockerImageName());
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(mysqlContainer.getJdbcUrl(), mysqlContainer.getUsername(),
                mysqlContainer.getPassword());
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
