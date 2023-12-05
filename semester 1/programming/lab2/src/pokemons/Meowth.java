package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;

public class Meowth extends Pokemon {
    public Meowth(String name, int level) {
        super(name, level);
        setType(Type.NORMAL);
        setStats(40, 45, 35, 40, 40, 90);
        setMove(new ThunderShock(), new HydroPump(), new LightScreen());
    }
}
