package calendar;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
//				h2
//				.applySettings(HibernateConfigProperties.getH2Properties())
//				MySql
				.applySettings(HibernateConfigProperties.getMySqlProperties())
//				PostgreSQL
//				.applySettings(HibernateConfigProperties.getPostgreSqlProperties())
				.build();
//		final Connection connection = serviceRegistry
//				.getService(ConnectionProvider.class)
//				.getConnection();
//		Runnable openConsole = new Runnable() {
//			
//			@Override
//			public void run() {
//				try {
//					org.h2.tools.Server.startWebServer(connection);
//				}catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//			}
//		};
		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		Session session = sessionFactory.openSession();
		System.out.println("Begin transaction...");
		Transaction transaction = session.beginTransaction();
		//create a user
		User user = new User();
		user.setUserName("Dario Mariani");
		//create some events
		List<Event> events = new ArrayList<>();
		//soccer match
		Event event1 = new Event();
		event1.setStartsAt(LocalDateTime.of(2023, 3, 13, 18, 0));
		event1.setEndsAt(LocalDateTime.of(2023, 3, 13, 19, 0));
		event1.setName("Soccer Match");
		event1.setOwner(user);
		events.add(event1);
		//exam
		Event event2 = new Event();
		event2.setStartsAt(LocalDateTime.of(2023, 3, 13, 9, 0));
		event2.setEndsAt(LocalDateTime.of(2023, 3, 13, 10, 0));
		event2.setName("Attsw Exam");
		event2.setOwner(user);
		events.add(event2);
		//exam
		Event event3 = new Event();
		event3.setStartsAt(LocalDateTime.of(2023, 3, 13, 21, 0));
		event3.setEndsAt(LocalDateTime.of(2023, 3, 14, 0, 0));
		event3.setName("Romantic Dinner");
		event3.setOwner(user);
		events.add(event3);
		//assign events to owner
		user.setEvents(events);
		session.persist(user);
		transaction.commit();
		System.out.println("User = " + "\n" + user.toString());
//		new Thread(openConsole).run();
	}
	

}
