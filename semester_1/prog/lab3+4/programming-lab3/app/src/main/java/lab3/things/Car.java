package lab3.things;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lab3.creature.properties.Existence;
import lab3.creature.properties.Moveable;

public class Car implements Existence, Moveable {
    private String name;
    private Existence currentLocation;

    public Car(String name) {
        this.name = name;
    }

    public Car(String name, Existence currentLocation) {
        this.name = name;
        this.currentLocation = currentLocation;
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
    public void move(Existence location) {
        System.out.println(toString() + " приехала в " + location.toString());
        currentLocation = location;
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(9, 79).
            append(name).append(currentLocation).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Car))
            return false;
        if (obj == this)
            return true;
        
        var rhs = (Car) obj;
        return new EqualsBuilder().append(name, rhs.name).append(currentLocation, rhs.currentLocation).isEquals();
    }
}
