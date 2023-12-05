package attacks;

import ru.ifmo.se.pokemon.*;

public class ShadowPunch extends PhysicalMove {
    public ShadowPunch() {
        super(Type.GHOST, 60, 100);
    }

    @Override
    public boolean checkAccuracy(Pokemon att, Pokemon def) {
        return true;
    }

    @Override
    public String describe() {
        return "used Shadow Punch (attack ignores changes to the accuracy and evasion stats)";
    }
}
