package client.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class ColorUtil {
    public static double[] toColor(String string) {
        double[] rgb = new double[3];

        String hex = DigestUtils.md5Hex(string);
        rgb[0] = Integer.parseInt(hex.substring(0, 2), 16) * 1.0 / 255;
        rgb[1] = Integer.parseInt(hex.substring(2, 4), 16) * 1.0 / 255;
        rgb[2] = Integer.parseInt(hex.substring(4, 6), 16) * 1.0 / 255;

        return rgb;
    }
}
