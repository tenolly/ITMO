import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;


class Main {
    public static void main(String[] args) {
        var c = new long[11];
        for (int i = 0; i < 11; ++i) {
            c[i] = (11 - i) * 2;
        }

        final double MIN = -3.0, MAX = 15.0;
        var x = new double[15];
        for (int i = 0; i < 15; ++i) {
            x[i] = (Math.random() * (MAX - MIN)) + MIN;
        }

        var values = new ArrayList<>(Arrays.asList(10L, 12L, 16L, 20L, 22L));
        var matrix = new double[11][15];
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                if (values.contains(c[i])) {
                    matrix[i][j] = Math.sin(Math.pow(Math.tan(x[j]) / 2, 2));
                } else if (c[i] == 14) {
                    matrix[i][j] = Math.cos(Math.pow((Math.cbrt(x[j]) + 2.0/3) / Math.sin(x[j]), Math.tan(x[j])));
                } else {
                    matrix[i][j] = Math.pow(2 / Math.cos(Math.pow(Math.cos(x[j]), 0.75 / Math.pow(1.0 / 3 * (x[j] - 0.5), 3))), 3);
                }
            }
        }

        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[i].length; ++j) {
                System.out.printf("%.5f ", matrix[i][j]);
            }
            System.out.print("\n");
        }
    }
}
