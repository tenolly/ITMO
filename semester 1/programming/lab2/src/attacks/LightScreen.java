package attacks;

import ru.ifmo.se.pokemon.*;

public class LightScreen extends StatusMove {
    public LightScreen() {
        super(Type.PSYCHIC, 0, 100);
    }

    @Override
    public String describe() {
        return "used Light Screen (has no effect)";
    }
}
