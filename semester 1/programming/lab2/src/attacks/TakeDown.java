package attacks;

import ru.ifmo.se.pokemon.*;

public class TakeDown extends PhysicalMove {
    public TakeDown() {
        super(Type.NORMAL, 90, 85);
    }

    @Override
    public void applySelfDamage(Pokemon att, double damage) {
        att.setMod(Stat.HP, (int) Math.round(damage * 0.25));
    }

    @Override
    public String describe() {
        return "used Take Down, receives 25% of damage in recoil";
    }
}
