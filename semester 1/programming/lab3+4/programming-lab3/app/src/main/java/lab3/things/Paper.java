package lab3.things;

import java.util.ArrayList;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lab3.things.properties.Readable;
import lab3.creature.properties.Existence;

public class Paper implements Readable, Existence  {
    static class Advertisement {
        private Existence object;
        private String text = "(у крабса все самое лучшее)";

        public Advertisement(Existence object) {
            this.object = object;
        }

        public String toString() {
            return object.toString() + " " + text + ". ";
        }
    }

    private ArrayList<Advertisement> story = new ArrayList<Advertisement>();
    private String name;
    private Existence currentLocation;

    public Paper(String name) {
        this.name = name;
    }

    public Paper(String name, Existence currentLocation) {
        this.name = name;
        this.currentLocation = currentLocation;
    }

    public void addObject(Existence object) {
        story.add(new Advertisement(object));
    }

    public String getText() {
        var text = new StringBuilder();
        story.forEach(text::append);
        return text.toString();
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
        return new HashCodeBuilder(17, 83).
            append(name).append(story).append(currentLocation).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Telegram))
            return false;
        if (obj == this)
            return true;
        
        var rhs = (Paper) obj;
        return new EqualsBuilder().append(name, rhs.name).append(currentLocation, rhs.currentLocation)
            .append(story, rhs.story).isEquals();
    }
}
