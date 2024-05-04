package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;

public class Ledian extends Pokemon {
    public Ledian(String name, int level) {
        super(name, level);
        setType(Type.BUG, Type.FLYING);
        setStats(55, 35, 50, 55, 110, 85);
        setMove(new ThunderShock(), new HydroPump(), new LightScreen(), new TakeDown());
    }
}
