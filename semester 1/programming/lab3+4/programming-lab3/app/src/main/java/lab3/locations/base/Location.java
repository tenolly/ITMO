package lab3.locations.base;

import lab3.creature.Human;
import lab3.creature.properties.Existence;

public abstract class Location implements Existence {
    public abstract void add(Human human);
    public abstract void remove(Human human);
}
