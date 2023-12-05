package attacks;

import ru.ifmo.se.pokemon.*;

public class FuryAttack extends PhysicalMove {
    public FuryAttack() {
        super(Type.NORMAL, 15, 85);
    }

    @Override
    public void applyOppDamage(Pokemon def, double damage) {
        int chance = (int) Math.floor(Math.random() / 0.125);
        int count = switch (chance) {
            case 0, 1, 2 -> 2;
            case 3, 4, 5 -> 3;
            case 6 -> 4;
            case 7 -> 5;
            default -> throw new IllegalArgumentException("Unexpected value: " + chance);
        };
        while (count != 0) {
            def.setMod(Stat.HP, (int) Math.round(damage)); 
            count--;
        }
    }

    @Override
    public String describe() {
        return "used Fury Attack";
    }
}
