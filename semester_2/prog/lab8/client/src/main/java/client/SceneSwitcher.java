package client;

import java.util.HashMap;

import client.ui.controllers.interfaces.Switchable;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {
    private static Stage primaryStage;
    private static HashMap<ScenesName, Scene> scenes = new HashMap<ScenesName, Scene>();
    private static HashMap<ScenesName, Object> controllers = new HashMap<ScenesName, Object>();

    public static void setPrimaryStage(Stage primaryStage) {
        SceneSwitcher.primaryStage = primaryStage;
    }

    public static void addScene(ScenesName name, URL resource) throws IOException {
        FXMLLoader loader = new FXMLLoader(resource);
        
        scenes.put(name, new Scene(loader.load()));
        controllers.put(name, loader.getController());
    }

    public static void switchTo(ScenesName name) {
        Scene scene = scenes.get(name);

        ((Switchable) controllers.get(name)).switchToThisControllerEvent();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
