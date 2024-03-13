package lab3.locations.base;

import java.util.ArrayList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lab3.creature.Human;
import lab3.creature.properties.Existence;

public class Building extends Location {
    private String name;
    private Existence currentLocation;
    private ArrayList<Human> peopleInside = new ArrayList<Human>();

    public Building(String name) {
        this.name = name;
    }

    @Override
    public void add(Human human) {
        System.out.println(human.toString() + " прибыл в " + this.toString());
        peopleInside.add(human);
    }

    @Override
    public void remove(Human human) {
        System.out.println(human.toString() + " ушел из " + this.toString());
        peopleInside.remove(human);
    }

    @Override
    public Existence getLocation() {
        return currentLocation;
    }

    @Override
    public void setLocation(Existence location) {
        currentLocation = location;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1, 55).
            append(name).append(currentLocation).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Building))
            return false;
        if (obj == this)
            return true;
        
        var rhs = (Building) obj;
        return new EqualsBuilder().append(name, rhs.name).append(currentLocation, rhs.currentLocation).isEquals();
    }
}
