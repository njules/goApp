package edu.kit.pse.gruppe1.goApp.server.database.management;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

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

	/**
	 * Factory Method
	 * 
	 * @return SessionFactory Object from Hibernate Library
	 */
	public static SessionFactory getFactory() {
		if (factory == null) {
			Configuration configuration = new Configuration();
			configuration.configure();
			configuration.addAnnotatedClass(Group.class);
			configuration.addAnnotatedClass(Event.class);		
			configuration.addAnnotatedClass(Location.class);
			configuration.addAnnotatedClass(Request.class);
			configuration.addAnnotatedClass(User.class);
			configuration.addAnnotatedClass(Participant.class);
			
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			factory = configuration.buildSessionFactory(serviceRegistry);
		}
		return factory;
	}

}