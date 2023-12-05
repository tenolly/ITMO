package attacks;

import ru.ifmo.se.pokemon.*;

public class RapidSpin extends PhysicalMove {
    public RapidSpin() {
        super(Type.NORMAL, 50, 100);
    }

    @Override
    public void applySelfEffects(Pokemon p) {
        p.setMod(Stat.SPEED, 1);
    }

    @Override
    public String describe() {
        return "used Rapid Spin, speed increased by one stage";
    }
}
