package attacks;

import ru.ifmo.se.pokemon.*;

public class FocusEnergy extends StatusMove {
    public FocusEnergy() {
        super(Type.NORMAL, 0, 100);
    }

    @Override
    public String describe() {
        return "used Focus Energy (has no effect)";
    }
}
