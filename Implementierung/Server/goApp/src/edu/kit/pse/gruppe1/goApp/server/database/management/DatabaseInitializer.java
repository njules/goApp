package edu.kit.pse.gruppe1.goApp.server.database.management;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import edu.kit.pse.gruppe1.goApp.server.EventDeletionTimer;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.Participant;
import edu.kit.pse.gruppe1.goApp.server.model.Request;
import edu.kit.pse.gruppe1.goApp.server.model.User;

/**
 * Factory for creating Session Factory for DB communication
 */
public class DatabaseInitializer {
    private static SessionFactory factory;
    private static EventDeletionTimer eventDeletionTimer;

    /**
     * Factory Method with return the SessionFactory and starts the eventDeletionTimer on the first
     * call
     * 
     * @return SessionFactory Object from Hibernate Library
     */
    public static synchronized SessionFactory getFactory() {
        if (factory == null) {
            Configuration configuration = new Configuration();
            configuration.configure();
            configuration.addAnnotatedClass(Group.class);
            configuration.addAnnotatedClass(Event.class);
            configuration.addAnnotatedClass(Location.class);
            configuration.addAnnotatedClass(Request.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Participant.class);
            configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            factory = configuration.buildSessionFactory(serviceRegistry);

            // start thread that delete old events
            eventDeletionTimer = new EventDeletionTimer(2, 1);
        }
        return factory;
    }
}