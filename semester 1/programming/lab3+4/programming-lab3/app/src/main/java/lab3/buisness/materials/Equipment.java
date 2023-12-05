package lab3.buisness.materials;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lab3.creature.Human;
import lab3.creature.properties.Existence;

public class Equipment implements Existence {
    private Human owner;
    private String name;
    private Existence currentLocation;

    public Equipment(String name, Human owner) {
        this.name = name;
        this.owner = owner;
    }

    public Human getOwner() {
        return owner;
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
        return new HashCodeBuilder(3, 101).
            append(name).append(owner).append(currentLocation).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Equipment))
            return false;
        if (obj == this)
            return true;
        
        var rhs = (Equipment) obj;
        return new EqualsBuilder().append(name, rhs.name).append(owner, rhs.owner).
            append(currentLocation, rhs.currentLocation).isEquals();
    }
}
