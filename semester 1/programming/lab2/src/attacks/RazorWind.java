package attacks;

import ru.ifmo.se.pokemon.*;


public class RazorWind extends SpecialMove {
    public RazorWind() {
        super(Type.NORMAL, 80, 100);
    }

    @Override
    public String describe() {
        return "used Razor Wind";
    }
}
