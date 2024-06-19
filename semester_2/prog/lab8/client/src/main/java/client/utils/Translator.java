package client.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Translator {
    private ResourceBundle bundle;

    public Translator(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
    public String getString(String key) {
        if (key == null) return "";
        return bundle.getString(key);
    }

    public String getStringOrReturnItBack(String key) {
        if (key == null) return "";
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public String formatDateTime(ZonedDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(bundle.getLocale());
        return date.format(formatter);
    }
}
