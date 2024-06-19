package client.ui.controllers.interfaces;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import client.ClientSession;
import client.utils.Translator;

public interface MultiLang {
    @SuppressWarnings("deprecation")
    public final HashMap<String, Locale> localesMap = new HashMap<>() {{
        put("Русский", new Locale("ru", "RU"));
        put("Français", new Locale("fr", "FR"));
        put("Español", new Locale("es", "ES"));
        put("Norsk", new Locale("no", "NO"));
    }};

    public static final Translator translator = new Translator(
        ResourceBundle.getBundle("locales/lang", localesMap.get(ClientSession.getCurrentLocale())));

    public void changeLanguage();
}
