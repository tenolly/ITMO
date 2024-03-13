package lab5.vehicle;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.function.Consumer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Function;

import lab5.vehicle.components.Coordinates;
import lab5.vehicle.components.FuelType;

/**
* Data object stores in the collection
*/
public class Vehicle implements Comparable<Vehicle> {
    private long id;
    private String name;
    private Coordinates coordinates;
    private java.time.ZonedDateTime creationDate;
    private Float enginePower;
    private Integer numberOfWheels;
    private float fuelConsumption;
    private FuelType fuelType;

    private static long currentMaxId = 0;
    private static Map<String, Consumer<String>> mutableFields = new HashMap<>();

    @JsonCreator
    public Vehicle(
        @JsonProperty("name") String name,
        @JsonProperty("coordinates") Coordinates coordinates,
        @JsonProperty("enginePower") Float enginePower,
        @JsonProperty("numberOfWheels") Integer numberOfWheels,
        @JsonProperty("fuelConsumption") float fuelConsumption,
        @JsonProperty("fuelType") FuelType fuelType) throws CasterException {

        setName(name);
        setCoordinates(coordinates);
        setEnginePower(enginePower);
        setNumberOfWheels(numberOfWheels);
        setFuelConsumption(fuelConsumption);
        setFuelType(fuelType);
        this.id = ++currentMaxId;
        this.creationDate = ZonedDateTime.now();

        mutableFields.put("name", arg -> setName(Caster.castToName.apply(arg)));
        mutableFields.put("x", arg -> coordinates.setX(Coordinates.Caster.castToX.apply(arg)));
        mutableFields.put("y", arg -> coordinates.setY(Coordinates.Caster.castToY.apply(arg)));
        mutableFields.put("engine power", arg -> setEnginePower(Caster.castToEnginePower.apply(arg)));
        mutableFields.put("number of wheels", arg -> setNumberOfWheels(Caster.castToNumberOfWheels.apply(arg)));
        mutableFields.put("fuel consumption", arg -> setFuelConsumption(Caster.castToFuelConsumption.apply(arg)));
        mutableFields.put("fuel type", arg -> setFuelType(Caster.castToFuelType.apply(arg)));
    }

    public static Set<String> getMutableFields() {
        return mutableFields.keySet();
    }

    public void setFieldByName(String name, String value) throws CasterException {
        mutableFields.get(name).accept(value);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public java.time.ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Float getEnginePower() {
        return enginePower;
    }

    public Integer getNumberOfWheels() {
        return numberOfWheels;
    }

    public float getFuelConsumption() {
        return fuelConsumption;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setName(String name) {
        Validator.validateName(name);
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        Validator.validateCoordinates(coordinates);
        this.coordinates = coordinates;
    }

    public void setEnginePower(Float enginePower) {
        Validator.validateEnginePower(enginePower);
        this.enginePower = enginePower;
    }

    public void setNumberOfWheels(Integer numberOfWheels) {
        Validator.validateNumberOfWheels(numberOfWheels);
        this.numberOfWheels = numberOfWheels;
    }

    public void setFuelConsumption(float fuelConsumption) {
        Validator.validateFuelConsumption(fuelConsumption);
        this.fuelConsumption = fuelConsumption;
    }

    public void setFuelType(FuelType fuelType) {
        Validator.validateFuelType(fuelType);
        this.fuelType = fuelType;
    }

    public static void setCurrentMaxId(long currentMaxId) {
        Vehicle.currentMaxId = currentMaxId;
    }

    @Override
    public String toString() {
        return
            "id=" + getId() +
            " name=" + getName() +
            " coordinates={x=" + coordinates.getX() + ", " + coordinates.getY() + "}" +
            " creationDate=" + getCreationDate() +
            " enginePower=" + getEnginePower() +
            " numberOfWheels=" + getNumberOfWheels() +
            " fuelConsumption=" + getFuelConsumption() +
            " fuelType=" + getFuelType();
    }

    @Override
    public int compareTo(Vehicle v) {
        return ((Long) getId()).compareTo(v.getId());
    }

    public static class Caster {
        public static final Function<String, Long> castToId = s -> {
            Long id = Long.parseLong(s);
            return id;
        };

        public static final Function<String, String> castToName = s -> {
            Vehicle.Validator.validateName(s);
            return s;
        };

        public static final Function<String, Float> castToEnginePower = s -> {
            var enginePower = Float.parseFloat(s.replace(",", "."));
            Vehicle.Validator.validateEnginePower(enginePower);
            return enginePower;
        };

        public static final Function<String, Integer> castToNumberOfWheels = s -> {
            Integer numberOfWheels = s.isEmpty() ? null : Integer.parseInt(s);
            Vehicle.Validator.validateNumberOfWheels(numberOfWheels);
            return numberOfWheels;
        };

        public static final Function<String, Float> castToFuelConsumption = s -> {
            var fuelConsumption = Float.parseFloat(s.replace(",", "."));
            Vehicle.Validator.validateFuelConsumption(fuelConsumption);
            return fuelConsumption;
        };

        public static final Function<String, FuelType> castToFuelType = s -> {
            FuelType fuelType;
            if (Validator.fuelTypeValues.contains(s)) fuelType = FuelType.valueOf(s);
            else fuelType = FuelType.valueOf(Validator.fuelTypeValues.get(Integer.parseInt(s) - 1));

            Vehicle.Validator.validateFuelType(fuelType);
            return fuelType;
        };
    }

    public static class Validator {
        public static String nameHumanIntelligibleConditions = "(string)";
        public static void validateName(String name) throws CasterException {
            if (name == null) throw new CasterException("name can't be null");
            if (name.isEmpty()) throw new CasterException("name can't be empty");
        }

        public static void validateCoordinates(Coordinates coordinates) throws CasterException {
            if (coordinates == null) throw new CasterException("coordinates can't be null");
        }

        public static String enginePowerHumanIntelligibleConditions = "(float, more than 0)";
        public static void validateEnginePower(Float enginePower) throws CasterException {
            if (enginePower == null) throw new CasterException("enginePower can't be null");
            if (enginePower <= 0) throw new CasterException("enginePower must be more than 0");
        }

        public static String numberOfWheelsHumanIntelligibleConditions = "(integer, more than 0, you can skip it)";
        public static void validateNumberOfWheels(Integer numberOfWheels) throws CasterException {
            if (numberOfWheels == null) return;
            if (numberOfWheels <= 0) throw new CasterException("numberOfWheels must be more than 0 or null");
        }

        public static String fuelConsumptionHumanIntelligibleConditions = "(float, more than 0)";
        public static void validateFuelConsumption(float fuelConsumption) throws CasterException {
            if (fuelConsumption <= 0) throw new CasterException("fuelConsumption must be more than 0");
        }

        public static ArrayList<String> fuelTypeValues = new ArrayList<String>();
        static { for (var value : FuelType.values()) fuelTypeValues.add(value.toString()); }
        public static String fuelTypeHumanIntelligibleConditions = "(" + String.join(", ", fuelTypeValues) + ")";
        public static void validateFuelType(FuelType fuelType) throws CasterException {
            if (fuelType == null) throw new CasterException("fuelType can't be null");
        }
    }
}
