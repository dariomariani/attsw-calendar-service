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

}
