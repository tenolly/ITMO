package lab5.vehicle.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Function;

import lab5.vehicle.CasterException;

/**
* Store coordinates
*/
public class Coordinates {
    private double x;
    private float y;

    @JsonCreator
    public Coordinates(@JsonProperty("x") double x, @JsonProperty("y") float y) throws CasterException {
        setX(x);
        setY(y);
    }

    public double getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void setX(double x) {
        Validator.validateX(x);
        this.x = x;
    }

    public void setY(float y) {
        Validator.validateY(y);
        this.y = y;
    }

    public static class Caster {
        public static final Function<String, Double> castToX = s -> {
            var x = Double.parseDouble(s.replace(",", "."));
            Coordinates.Validator.validateX(x);
            return x;
        };

        public static final Function<String, Float> castToY = s -> {
            var y = Float.parseFloat(s.replace(",", "."));
            Coordinates.Validator.validateY(y);
            return y;
        };
    }
    
    public static class Validator {
        public static String xHumanIntelligibleConditions = "(float, more than -809)";
        public static void validateX(Double x) throws CasterException {
            if (x <= -809) throw new CasterException("x must be more than -809");
        }

        public static String yHumanIntelligibleConditions = "(float, more than -429)";
        public static void validateY(Float y) throws CasterException {
            if (y <= -429) throw new CasterException("y must be more than -429");
        }
    }
}
