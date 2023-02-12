package persistence;


import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
	
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			Configuration configuration = new Configuration();
			configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
			configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
			configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb");
			configuration.setProperty("hibernate.connection.username", "sa");
			configuration.setProperty("hibernate.connection.password", "");
			configuration.setProperty("hibernate.show_sql", "true");
			configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
			configuration.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.ThreadLocalSessionContext");
			configuration.addAnnotatedClass(models.User.class);
			//configuration.addAnnotatedClass(Event.class);
			configuration.addPackage("models");

			StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			
			// Create MetadataSources
            MetadataSources sources = new MetadataSources(serviceRegistry);

            // Create Metadata
            Metadata metadata = sources.getMetadataBuilder().build();

			//return configuration.buildSessionFactory(serviceRegistry);
            return metadata.getSessionFactoryBuilder().build();
		} catch (HibernateException ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	 public static SessionFactory getSessionFactory() {
	      return sessionFactory;
	   }

}
