package server.managers;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import server.ServerApp;
import utils.database.User;
import utils.database.Vehicle;
import utils.database.components.vehicle.Coordinates;

public class HibernateUtil {
    private final static Logger LOGGER = Logger.getLogger(ServerApp.class.getName());
    
    private static SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() throws ExceptionInInitializerError {
        try {
            Properties properties = new Properties();
            properties.load(HibernateUtil.class.getResourceAsStream("/hibernate.properties"));

            Configuration configuration = new Configuration();
            configuration.configure("/hibernate.cfg.xml").addProperties(properties);

            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Vehicle.class);
            configuration.addAnnotatedClass(Coordinates.class);

            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
            
            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
            throw new ExceptionInInitializerError("Initial SessionFactory failed" + e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}