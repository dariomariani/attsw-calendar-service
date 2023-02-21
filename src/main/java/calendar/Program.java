package calendar;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import models.Event;
import models.User;
import repository.impl.EventRepositoryImpl;
import repository.impl.UserRepositoryImpl;

public class Program {

	public static void main(String[] args) throws SQLException {
		System.out.println("Calendar App is running...");
		// jakarta
		var entityManagerFactory = Persistence.createEntityManagerFactory("h2");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Connection connection = entityManager.unwrap(Session.class).doReturningWork(conn -> conn);
		Runnable openConsole = new Runnable() {

			@Override
			public void run() {
				try {
					org.h2.tools.Server.startWebServer(connection);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};
		User user = new User();
		user.setUsername("Dario Mariani");
		// create some events
		List<Event> events = new ArrayList<>();
		// soccer match
		Event event1 = new Event();
		event1.setStartsAt(LocalDateTime.of(2023, 3, 13, 18, 0));
		event1.setEndsAt(LocalDateTime.of(2023, 3, 13, 19, 0));
		event1.setName("Soccer Match");
		event1.setOwner(user);
		events.add(event1);
		// exam
		Event event2 = new Event();
		event2.setStartsAt(LocalDateTime.of(2023, 3, 13, 9, 0));
		event2.setEndsAt(LocalDateTime.of(2023, 3, 13, 10, 0));
		event2.setName("Attsw Exam");
		event2.setOwner(user);
		events.add(event2);
		// dinner
		Event event3 = new Event();
		event3.setStartsAt(LocalDateTime.of(2023, 3, 13, 21, 0));
		event3.setEndsAt(LocalDateTime.of(2023, 3, 14, 0, 0));
		event3.setName("Romantic Dinner");
		event3.setOwner(user);
		events.add(event3);
		// assign events to owner
		user.setEvents(events);
		UserRepositoryImpl userRepository = new UserRepositoryImpl(entityManagerFactory);
		EventRepositoryImpl eventRepository = new EventRepositoryImpl(entityManagerFactory);
		userRepository.save(user);
		System.out.println("UserID = " + user.getId());
		
		var result = eventRepository.findAll();
		System.out.println("Events found: " + result.stream().map(Event::toString).collect(Collectors.joining(", ")));
		
		var resultUser = userRepository.findById(user.getId());
		System.out.println("Users found: " + resultUser.toString());
		new Thread(openConsole).run();
	}

}
