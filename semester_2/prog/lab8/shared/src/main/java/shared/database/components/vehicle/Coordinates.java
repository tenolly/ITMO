package shared.database.components.vehicle;

import java.io.Serializable;

import com.google.common.base.Function;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import shared.caster.CasterException;

/**
* Store coordinates
*/
@Entity
@Table(name = "coordinates")
public class Coordinates implements Serializable {
    @Id   
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column
    private double x;

    @Column
    private float y;

    public Coordinates() {}

    public Coordinates(double x,float y) throws CasterException {
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

    public static class Caster implements Serializable {
        public static final Function<String, Double> castToX = (Function<String, Double> & Serializable) s -> {
            var x = Double.parseDouble(s.replace(",", "."));
            Coordinates.Validator.validateX(x);
            return x;
        };

        public static final Function<String, Float> castToY = (Function<String, Float> & Serializable) s -> {
            var y = Float.parseFloat(s.replace(",", "."));
            Coordinates.Validator.validateY(y);
            return y;
        };
    }
    
    public static class Validator implements Serializable {
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
