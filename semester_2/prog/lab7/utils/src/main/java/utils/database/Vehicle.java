package utils.database;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Function;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import utils.caster.CasterException;
import utils.database.components.vehicle.Coordinates;
import utils.database.components.vehicle.FuelType;

/**
* Data object stores in the collection
*/

@Entity
@Table(name = "vehicles")
public class Vehicle implements Comparable<Vehicle>, Serializable {
    @Id   
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String name;

    @OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "coordinates_id")
    private Coordinates coordinates;

    @Column(name = "creation_date")
    private java.time.ZonedDateTime creationDate;

    @Column(name = "engine_power")
    private Float enginePower;

    @Column(name = "number_of_wheels")
    private Integer numberOfWheels;

    @Column(name = "fuel_consumption")
    private float fuelConsumption;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Column(name = "username")
    private String username;

    private static HashSet<String> mutableFieldsSet = new HashSet<String>();
    static {
        mutableFieldsSet.add("name");
        mutableFieldsSet.add("x");
        mutableFieldsSet.add("y");
        mutableFieldsSet.add("enginePower");
        mutableFieldsSet.add("numberOfWheels");
        mutableFieldsSet.add("fuelConsumption");
        mutableFieldsSet.add("fuelType");
    }

    public Vehicle() {}

    @JsonCreator
    public Vehicle(
        @JsonProperty("name") String name,
        @JsonProperty("coordinates") Coordinates coordinates,
        @JsonProperty("enginePower") Float enginePower,
        @JsonProperty("numberOfWheels") Integer numberOfWheels,
        @JsonProperty("fuelConsumption") float fuelConsumption,
        @JsonProperty("fuelType") FuelType fuelType,
        @JsonProperty("username") String username) throws CasterException {

        setName(name);
        setCoordinates(coordinates);
        setEnginePower(enginePower);
        setNumberOfWheels(numberOfWheels);
        setFuelConsumption(fuelConsumption);
        setFuelType(fuelType);
        this.username = username;
        this.creationDate = ZonedDateTime.now();
    }

    public static HashSet<String> getMutableFields() {
        HashSet<String> map = new HashSet<>();
        mutableFieldsSet.forEach(elem -> map.add(elem));
        return map;
    }

    public void setFieldByName(String name, String value) throws CasterException {
        switch (name) {
            case "name":
                setName(Caster.castToName.apply(value));
                break;
            case "x":
                coordinates.setX(Coordinates.Caster.castToX.apply(value));
                break;
            case "y":
                coordinates.setY(Coordinates.Caster.castToY.apply(value));
                break;
            case "enginePower":
                setEnginePower(Caster.castToEnginePower.apply(value));
                break;
            case "numberOfWheels":
                setNumberOfWheels(Caster.castToNumberOfWheels.apply(value));
                break;
            case "fuelConsumption":
                setFuelConsumption(Caster.castToFuelConsumption.apply(value));
                break;
            case "fuelType":
                setFuelType(Caster.castToFuelType.apply(value));
                break;
        }
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

    public String getUsername() {
        return username;
    }

    public Object getFieldValue(String name) {
        switch (name) {
            case "name":
                return this.getName();
            case "x":
                return this.getCoordinates().getX();
            case "y":
                return this.getCoordinates().getY();
            case "enginePower":
                return this.getEnginePower();
            case "numberOfWheels":
                return this.getNumberOfWheels();
            case "fuelConsumption":
                return this.getFuelConsumption();
            case "fuelType":
                return this.getFuelType();
        }

        return null;
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
            " fuelType=" + getFuelType() +
            " username=" + getUsername();
    }

    @Override
    public int compareTo(Vehicle v) {
        return ((String) getName()).compareTo(v.getName());
    }

    public static class Caster implements Serializable {
        public static final Function<String, Long> castToId = (Function<String, Long> & Serializable) s -> {
            Long id = Long.parseLong(s);
            return id;
        };

        public static final Function<String, String> castToName = (Function<String, String> & Serializable) s -> {
            Vehicle.Validator.validateName(s);
            return s;
        };

        public static final Function<String, Float> castToEnginePower = (Function<String, Float> & Serializable) s -> {
            var enginePower = Float.parseFloat(s.replace(",", "."));
            Vehicle.Validator.validateEnginePower(enginePower);
            return enginePower;
        };

        public static final Function<String, Integer> castToNumberOfWheels = (Function<String, Integer> & Serializable) s -> {
            Integer numberOfWheels = s.isEmpty() ? null : Integer.parseInt(s);
            Vehicle.Validator.validateNumberOfWheels(numberOfWheels);
            return numberOfWheels;
        };

        public static final Function<String, Float> castToFuelConsumption = (Function<String, Float> & Serializable) s -> {
            var fuelConsumption = Float.parseFloat(s.replace(",", "."));
            Vehicle.Validator.validateFuelConsumption(fuelConsumption);
            return fuelConsumption;
        };

        public static final Function<String, FuelType> castToFuelType = (Function<String, FuelType> & Serializable) s -> {
            FuelType fuelType;
            if (Validator.fuelTypeValues.contains(s)) fuelType = FuelType.valueOf(s);
            else {
                if (Integer.parseInt(s) > 3) throw new CasterException("too big number");
                fuelType = FuelType.valueOf(Validator.fuelTypeValues.get(Integer.parseInt(s) - 1));
            };

            Vehicle.Validator.validateFuelType(fuelType);
            return fuelType;
        };
    }

    public static class Validator implements Serializable {
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
