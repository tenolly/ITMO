package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.Type;
import ru.ifmo.se.pokemon.Pokemon;

public class Burmy extends Pokemon {
    public Burmy(String name, int level) {
        super(name, level);
        setType(Type.BUG);
        setStats(40, 29, 45, 29, 45, 36);
        setMove(new FocusEnergy(), new ShadowPunch(), new FuryAttack());
    }
}
