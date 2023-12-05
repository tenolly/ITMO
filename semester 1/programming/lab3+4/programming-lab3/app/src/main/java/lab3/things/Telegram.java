package lab3.things;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lab3.creature.properties.Existence;
import lab3.things.properties.Readable;

public class Telegram implements Readable, Existence {
    private String name;
    private String text;
    private Existence currentLocation;

    public Telegram(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public Telegram(String name, String text, Existence currentLocation) {
        this.name = name;
        this.text = text;
        this.currentLocation = currentLocation;
    }

    public String getText() {
        return text;
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
        return new HashCodeBuilder(13, 93).
            append(name).append(text).append(currentLocation).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Telegram))
            return false;
        if (obj == this)
            return true;
        
        var rhs = (Telegram) obj;
        return new EqualsBuilder().append(name, rhs.name).
            append(text, rhs.text).append(currentLocation, rhs.currentLocation).isEquals();
    }
}
