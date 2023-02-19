package persistence;

import java.util.Properties;

public class HibernateConfigProperties {

	public static Properties getH2Properties() {
		Properties prop = new Properties();
		prop.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		prop.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
		prop.setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb");
		prop.setProperty("hibernate.connection.username", "sa");
		prop.setProperty("hibernate.connection.password", "");
		prop.setProperty("hibernate.show_sql", "true");
		prop.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		prop.setProperty("hibernate.current_session_context_class",
				"org.hibernate.context.internal.ThreadLocalSessionContext");
		return prop;
	}
	
	public static Properties getMySqlProperties() {
		Properties prop = new Properties();
		prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		prop.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
		prop.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/calendarmysql");
		prop.setProperty("hibernate.connection.username", "mysqluser");
		prop.setProperty("hibernate.connection.password", "mysqlpassword");
		prop.setProperty("hibernate.show_sql", "true");
		prop.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		prop.setProperty("hibernate.current_session_context_class",
				"org.hibernate.context.internal.ThreadLocalSessionContext");
		return prop;
	}
	
	public static Properties getPostgreSqlProperties() {
		Properties prop = new Properties();
		prop.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		prop.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
		prop.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/postgres?currentSchema=calendar");
		prop.setProperty("hibernate.connection.username", "psqluser");
		prop.setProperty("hibernate.connection.password", "psqlpassword");
		prop.setProperty("hibernate.show_sql", "true");
		prop.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		prop.setProperty("hibernate.current_session_context_class",
				"org.hibernate.context.internal.ThreadLocalSessionContext");
		return prop;
	}

}
