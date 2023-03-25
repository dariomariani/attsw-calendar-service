package containers;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestContainers {
	
	private static String DOCKER_COMPOSE_FILE_PATH;
	private static final Logger logger = Logger.getLogger(TestContainers.class.getName());

    /*@Container
    private static final DockerComposeContainer<?> dockerComposeContainer = new DockerComposeContainer<>(new File(DOCKER_COMPOSE_FILE_PATH))
            .withExposedService("postgresql", 5432)
            .withExposedService("mysql", 3306);*/

	@Container
	private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:13-alpine")
	        .withDatabaseName("calendar").withUsername("psqluser").withPassword("psqlpassword").withExposedPorts(5432);

	@Container
	private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0").withDatabaseName("calendar")
	        .withUsername("mysqluser").withPassword("mysqlpassword").withEnv("MYSQL_ROOT_PASSWORD", "rootpassword").withExposedPorts(3306);

	private static final JdbcDatabaseContainer<?>[] CONTAINERS = { postgresqlContainer, mysqlContainer };
	
	@BeforeAll
    public static void setup() {
		String dbProvider = System.getProperty("dbprovider");
		DOCKER_COMPOSE_FILE_PATH = String.format("src/test/resources/docker-compose-%s.yml", dbProvider);
		logger.info("Running TestContainers with DB: " + dbProvider + " ...");
    }

	@Test
	public void testConnection() throws SQLException {
		logger.info("Starting test connection...");
		for (JdbcDatabaseContainer<?> container : CONTAINERS) {
			logger.info("Starting container " + container.getDockerImageName());
			container.start();
			Connection connection = DriverManager.getConnection(container.getJdbcUrl(), container.getUsername(),
					container.getPassword());
			assertEquals("calendar", connection.getCatalog());
			container.stop();
		}
	}

}
