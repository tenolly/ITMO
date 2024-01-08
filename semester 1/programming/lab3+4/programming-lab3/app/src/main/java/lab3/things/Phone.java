package lab3.things;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lab3.creature.properties.Existence;
import lab3.things.properties.Functionality;

public class Phone implements Existence {
    public Functionality functionality = new Functionality() {
        private boolean isBroken = false;

        public void repair() {
            isBroken = false;
        }

        public void broke() {
            isBroken = true;
        }

        public boolean checkFunctionality() {
            return isBroken;
        }
    };
    private String name;
    private Existence currentLocation;

    public Phone(String name) {
        this.name = name;
    }

    public Phone(String name, Existence currentLocation) {
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
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 81).
            append(name).append(currentLocation).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Phone))
            return false;
        if (obj == this)
            return true;
        
        var rhs = (Phone) obj;
        return new EqualsBuilder().append(name, rhs.name).append(currentLocation, rhs.currentLocation).isEquals();
    }
}
