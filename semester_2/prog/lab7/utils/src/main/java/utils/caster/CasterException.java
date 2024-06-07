package utils.caster;

/**
* Exception throws when cast to another type is impossible
*/
public class CasterException extends IllegalArgumentException {
    public CasterException(String string) {
        super(string);
    }
}
