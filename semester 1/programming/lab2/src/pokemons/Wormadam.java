package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;

public class Wormadam extends Pokemon {
    public Wormadam(String name, int level) {
        super(name, level);
        setType(Type.BUG, Type.GRASS);
        setStats(60, 59, 85, 79, 105, 36);
        setMove(new FocusEnergy(), new ShadowPunch(), new FuryAttack(), new FocusEnergy());
    }
}
