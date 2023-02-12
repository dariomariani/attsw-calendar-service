package calendar;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import models.Event;
import models.User;
import persistence.HibernateConfigProperties;

public class Program {

	public static void main(String[] args) throws SQLException {
		System.out.println("Calendar App is running...");
		Configuration configuration = new Configuration()
				.addAnnotatedClass(User.class)
				.addAnnotatedClass(Event.class);
		StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(HibernateConfigProperties.getH2Properties())
				.build();
		final Connection connection = serviceRegistry
				.getService(ConnectionProvider.class)
				.getConnection();
		Runnable openConsole = new Runnable() {
			
			@Override
			public void run() {
				try {
					org.h2.tools.Server.startWebServer(connection);
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		};
		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		Session session = sessionFactory.openSession();
		System.out.println("Begin transaction...");
		Transaction transaction = session.beginTransaction();
		User user = new User();
		user.setUserName("Dario Mariani");
		session.persist(user);
		transaction.commit();
		System.out.println("User ID=" + user.getId());
		new Thread(openConsole).run();
	}
	

}
