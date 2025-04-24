package web.api.auth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.SignatureAlgorithm;

public class AuthConfig {
    private static final String CONFIG_FILE_PATH = "auth.properties";
    private static Properties properties = new Properties();

    static {
        try (InputStream file = AuthConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)) {
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config properties.");
        }
    }

    public static String getSecretKey() {
        return properties.getProperty("SECRET_KEY");
    }

    public static SecretKeySpec getSecretKeySpec() {
        return new SecretKeySpec(Base64.getDecoder().decode(properties.getProperty("SECRET_KEY")), SignatureAlgorithm.HS256.getJcaName());
    }
}