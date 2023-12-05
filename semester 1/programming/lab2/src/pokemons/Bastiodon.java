package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;

public class Bastiodon extends Pokemon {
    public Bastiodon(String name, int level) {
        super(name, level);
        setType(Type.ROCK, Type.STEEL);
        setStats(60, 52, 168, 47, 138, 30);
        setMove(new FocusEnergy(), new ShadowPunch());
    }
}
