package lab3.creature.properties;

import lab3.creature.Human;
import lab3.locations.base.Location;
import lab3.things.Phone;
import lab3.things.problems.PhoneIsBrokenException;

public interface CanCall {
    public void call(Phone phone, Human human, Location location) throws PhoneIsBrokenException;
}
