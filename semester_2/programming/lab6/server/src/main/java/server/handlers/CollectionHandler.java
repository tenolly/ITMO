package server.handlers;

import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.PriorityQueue;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.Function;

import utils.vehicle.Vehicle;

/**
* Stores and provides interaction with the collection
*/
public class CollectionHandler {
    private PriorityQueue<Vehicle> collection = new PriorityQueue<Vehicle>();
    private Date createdAt;

    public CollectionHandler() {
        createdAt = new Date();
    }

    public CollectionHandler(String filename) {
        createdAt = new Date();
        try {
            File file = new File(filename);
            if (file.length() == 0) return;
            
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
            xmlMapper.findAndRegisterModules();

            PriorityQueue<Vehicle> values = xmlMapper.readValue(file, new TypeReference<PriorityQueue<Vehicle>>() {});
            ArrayList<Long> used_ids = new ArrayList<Long>();
            long maxId = 0;
            for (var value : values) {
                var valueId = value.getId();
                if (used_ids.contains(valueId)) throw new IOException("The collection has objects with same id");
                if (valueId > maxId) maxId = valueId;
                used_ids.add(valueId);
            }
            Vehicle.setCurrentMaxId(maxId);
            collection.addAll(values);
        } catch (IOException e) {
            System.out.println("\nLoad a collection from the file has failed, cause:\n" + e.getMessage() + "\n");
        }
    }

    public PriorityQueue<Vehicle> getCollection() {
        return collection;
    }

    public void removeElements(Function<Vehicle, Boolean> compareFunction) {
        collection.forEach(vehicle -> {
            if (compareFunction.apply(vehicle)) {
                collection.remove(vehicle);
            }
        });
    }

    // It's so weird, I know
    public void removeElements(Function<Vehicle, Boolean> compareFunction, final long count) {
        var wrapper = new Object(){ long count = 0; };
        try {
            collection.forEach(vehicle -> {
                if (wrapper.count == count) return;
                if (compareFunction.apply(vehicle)) {
                    collection.remove(vehicle);
                    ++wrapper.count;
                }
            });
        } catch (ConcurrentModificationException e) {
            return;
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

    public void add(Vehicle v) {
        collection.add(v);
    }

    public Vehicle getById(long id) {
        for (Vehicle vehicle : collection) {
            if (vehicle.getId() == id) return vehicle;
        }
        return null;
    }

    public Vehicle pop() {
        return collection.poll();
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

    public void clear() {
        collection.clear();
    }
}
