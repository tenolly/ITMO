public class Validator {
    public static boolean validateX(int x) {
        return x >= -3 && x <= 5;
    }

    public static boolean validateY(float y) {
        return y >= -5 && y <= 3;
    }

    public static boolean validateR(float r) {
        return r >= 1 && r <= 3 && r % 0.5 == 0;
    }
}
