package lab3.creature;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lab3.creature.properties.CanCall;
import lab3.creature.properties.Conscious;
import lab3.creature.properties.Existence;
import lab3.creature.properties.Moveable;
import lab3.creature.properties.UnderstandText;
import lab3.locations.base.Location;
import lab3.things.Phone;
import lab3.things.problems.PhoneIsBrokenException;
import lab3.things.properties.Readable;

public class Human implements UnderstandText, Moveable, CanCall, Conscious, Existence {
    private String name;
    private Existence currentLocation;

    public Human(String name) {
        this.name = name;
    }

    public Human(String name, Existence currentLocation) {
        this.name = name;
        this.currentLocation = currentLocation;
    }

    @Override
    public String read(Readable obj) {
        System.out.println(toString() + " прочитал " + obj.getText());
        return obj.getText();
    }

    @Override
    public void move(Existence location) {
        currentLocation = location;

        if (location instanceof Location) {
            ((Location) location).add(this);
        } else {
            System.out.println(toString() + " теперь находится в "  + location.toString());
        }
    }

    @Override
    public void mind(String thought) {
        System.out.println(this.name + " подумал: \"" + thought + "\"");
    }

    @Override
    public void call(Phone phone, Human human, Location location) throws PhoneIsBrokenException {
        if (!phone.functionality.checkFunctionality()) {
            throw new PhoneIsBrokenException();
        }
        System.out.println(toString() + " вызвал " + human.toString());
        location.add(human);
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
        return new HashCodeBuilder(7, 87).append(name).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Human))
            return false;
        if (obj == this)
            return true;
        
        var rhs = (Human) obj;
        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }
}