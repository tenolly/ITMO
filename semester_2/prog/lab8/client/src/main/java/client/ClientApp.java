package client;

import java.io.IOException;

import client.managers.ClientEnviroment;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClientApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setResizable(false);

        primaryStage.setTitle("DuckBase");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        SceneSwitcher.setPrimaryStage(primaryStage);

        SceneSwitcher.addScene(ScenesName.REGISTRATION, getClass().getResource("/ui/registration.fxml"));
        SceneSwitcher.addScene(ScenesName.ENTER, getClass().getResource("/ui/enter.fxml"));
        SceneSwitcher.addScene(ScenesName.MAIN_WINDOW, getClass().getResource("/ui/main.fxml"));

        SceneSwitcher.switchTo(ScenesName.ENTER);
    }
    
    public static void main(String[] args) {
        ClientSession.setCurrentLocale("Русский");
        ClientSession.setClientEnviroment(new ClientEnviroment());
        try {
            ClientSession.start("localhost", 2000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        launch(args);
    }
}
