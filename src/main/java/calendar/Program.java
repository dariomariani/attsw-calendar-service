package calendar;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import models.Event;
import models.User;
import org.hibernate.Session;
import repository.impl.EventRepositoryImpl;
import repository.impl.UserRepositoryImpl;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Program {

    private static final Logger logger = Logger.getLogger(Program.class.getName());

    public static void main(String[] args) {
        String dbProvider = System.getProperty("dbprovider");
        if (dbProvider == null || dbProvider.isEmpty()) dbProvider = "h2";
        logger.log(Level.INFO, "Calendar App is running on {0} ...", dbProvider);
        var entityManagerFactory = Persistence.createEntityManagerFactory(dbProvider);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Runnable openConsole = null;
        if (dbProvider.equals("h2")) {
            Connection connection = entityManager.unwrap(Session.class).doReturningWork(conn -> conn);
            openConsole = () -> {
                try {
                    org.h2.tools.Server.startWebServer(connection);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.getLocalizedMessage());
                }

            };
        }
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
        logger.info("UserID = " + user.getId());

        var result = eventRepository.findAll();
        logger.log(Level.INFO, "Events found: {0}", result.stream().map(Event::toString).collect(Collectors.joining(", ")));

        var resultUser = userRepository.findById(user.getId());
        logger.log(Level.INFO, "Users found: {0}", resultUser);
        if (dbProvider.equals("h2")) new Thread(openConsole).start();
    }

}
