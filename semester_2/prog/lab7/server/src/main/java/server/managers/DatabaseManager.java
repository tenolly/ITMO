package server.managers;

import java.util.Date;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.PriorityQueue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.common.base.Function;

import utils.database.User;
import utils.database.Vehicle;

/**
* Stores and provides interaction with the collection
*/
public class DatabaseManager {
    private PriorityQueue<Vehicle> collection = new PriorityQueue<Vehicle>();
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private Date createdAt;

    public DatabaseManager() {
        createdAt = new Date();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createQuery(
                "SELECT v FROM Vehicle v", 
                Vehicle.class
            ).getResultList().forEach(v -> collection.add(v));
        }
    }

    public boolean userExsists(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            var query = session.createQuery(
                "SELECT id FROM User WHERE username = :username AND hashedPassword = :hashedPassword", 
                Long.class
            )
            .setParameter("username", user.getUsername())
            .setParameter("hashedPassword", user.getHashedPassword());

            return query.getSingleResultOrNull() != null;
        }
    }

    public boolean usernameExsists(String username) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            var query = session.createQuery(
                "SELECT id FROM User WHERE username = :username", 
                Long.class
            )
            .setParameter("username", username);

            return query.getSingleResultOrNull() != null;
        }
    }

    public void addUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(user);

            session.getTransaction().commit();
        }
    }

    public PriorityQueue<Vehicle> getCollection() {
        return collection;
    }

    public void removeElements(Function<Vehicle, Boolean> compareFunction, String username) {
        ArrayList<Vehicle> removedVehicles = new ArrayList<Vehicle>();

        collection.forEach(vehicle -> {
            if (compareFunction.apply(vehicle)) {
                if (!vehicle.getUsername().equals(username)) return;
                removedVehicles.add(vehicle);
            }
        });

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            removedVehicles.forEach(v -> session.remove(v));

            session.getTransaction().commit();

            collection.removeAll(removedVehicles);
        }
    }

    // It's so weird, I know
    public void removeElements(Function<Vehicle, Boolean> compareFunction, String username, final long count) {
        ArrayList<Vehicle> removedVehicles = new ArrayList<Vehicle>();

        var wrapper = new Object(){ long count = 0; };
        try {
            collection.forEach(vehicle -> {
                if (wrapper.count == count) return;
                if (compareFunction.apply(vehicle)) {
                    if (!vehicle.getUsername().equals(username)) return;
                    removedVehicles.add(vehicle);
                    ++wrapper.count;
                }
            });
        } catch (ConcurrentModificationException e) {}

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            removedVehicles.forEach(v -> session.remove(v));

            session.getTransaction().commit();

            collection.removeAll(removedVehicles);
        }
    }

    public int countElements(Function<Vehicle, Boolean> compareFunction) {
        var wrapper = new Object(){ int count = 0; };
        collection.forEach(vehicle -> {
            if (compareFunction.apply(vehicle)) ++wrapper.count;
        });
        return wrapper.count;
    }

    public ArrayList<Vehicle> getElements(Function<Vehicle, Boolean> compareFunction) {
        ArrayList<Vehicle> elements = new ArrayList<Vehicle>();
        collection.forEach(vehicle -> {
            if (compareFunction.apply(vehicle)) elements.add(vehicle);
        });
        return elements;
    }

    public Vehicle getFirstElement(String username) {
        for (Vehicle vehicle : collection) {
            if (vehicle.getUsername().equals(username)) return vehicle;
        }
        return null;
    }

    public void add(Vehicle v) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(v);

            session.getTransaction().commit();

            collection.add(v);
        }
    }

    public void update(Vehicle v, String field, String value) {
        String prevValue = v.getFieldValue(field).toString();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.remove(v);

            v.setFieldByName(field, value);

            session.persist(v);

            session.getTransaction().commit();
        }  catch (Exception e) {
            v.setFieldByName(field, prevValue);
            throw e;
        }
    }

    public Vehicle getById(long id) {
        for (Vehicle vehicle : collection) {
            if (vehicle.getId() == id) return vehicle;
        }
        return null;
    }

    public Vehicle pop(String username) {
        Vehicle vehicle;

        try (Session session = sessionFactory.openSession()) {
            vehicle = getFirstElement(username);

            session.beginTransaction();

            session.remove(vehicle);

            session.getTransaction().commit();

            collection.remove(vehicle);
        }

        return vehicle;
    }

    public int size() {
        return collection.size();
    }

    public Date createdAt() {
        return createdAt;
    }

    public Class<?> type() {
        return collection.getClass();
    }

    public void clear(String username) {
        ArrayList<Vehicle> removedVehicles = new ArrayList<Vehicle>();

        for (Vehicle vehicle : collection) {
            if (vehicle.getUsername().equals(username)) {
                removedVehicles.add(vehicle);
            };
        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            removedVehicles.forEach(v -> session.remove(v));

            session.getTransaction().commit();

            collection.removeAll(removedVehicles);
        }
    }
}
