package utils.commands;

import java.io.Serializable;
import com.google.common.base.Function;

/**
* Class for command line arguments
*/
public class Argument<T> implements Serializable  {
    /**
    * The single argument alias used to refer to the argument
    */
    private String name;
    /**
    * The description says what user must to input
    */
    private String decription;
    /**
    * The value of the argument that must be obtained before it can be used
    */
    private T value;
    /**
    * The function cast value to right type
    */
    public Function<String, T> castToType;
    
    public Argument(String name, String decription, Function<String, T> castToType) {
        this.name = name;
        this.decription = decription;
        this.castToType = castToType;
    }

    public void setValue(String value) throws IllegalArgumentException {
        this.value = castToType.apply(value);
    }

    public T getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return decription;
    }
}
