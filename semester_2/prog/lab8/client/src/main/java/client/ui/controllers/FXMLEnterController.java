package client.ui.controllers;

import java.util.ResourceBundle;
import java.io.IOException;
import java.util.HashMap;

import client.ClientSession;
import client.SceneSwitcher;
import client.ScenesName;
import client.ui.controllers.interfaces.MultiLang;
import client.ui.controllers.interfaces.Switchable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import shared.database.User;
import shared.request.RequestStatus;

public class FXMLEnterController implements MultiLang, Switchable {
    @FXML private Label enterLabel;
    @FXML private TextField loginLine;
    @FXML private PasswordField passwordLine;
    @FXML private Button enterButton;
    @FXML private Button noAccountButton;
    @FXML private ComboBox<String> langComboBox;
    @FXML private Label errorLabel;

    private String currentError = "";

    @FXML
    void initialize() {
        langComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            translator.setBundle(ResourceBundle.getBundle("locales/lang", localesMap.get(newValue)));
            ClientSession.setCurrentLocale(newValue);
            changeLanguage();
        });

        enterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HashMap<String, Object> args = new HashMap<String, Object>();
                args.put("command", "verify");

                try {
                    ClientSession.setUser(new User(loginLine.getText(), passwordLine.getText()));
                    var response = ClientSession.makeRequest("auth", args);
                    if (response.getStatus().equals(RequestStatus.FULLFILED)) {
                        SceneSwitcher.switchTo(ScenesName.MAIN_WINDOW);
                    } else {
                        ClientSession.unsetUser();
                        alertError("IncorrectData");
                    }
                } catch (IOException e) {
                    ClientSession.unsetUser();
                    alertError("ServerIsUnavailableError");
                    e.printStackTrace();
                }
            }
        });

        noAccountButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SceneSwitcher.switchTo(ScenesName.REGISTRATION);
            }
        });
    }

    public void changeLanguage() {
        langComboBox.setValue(ClientSession.getCurrentLocale());
        enterLabel.setText(translator.getString("Enter"));
        loginLine.setPromptText(translator.getString("EnterLogin"));
        passwordLine.setPromptText(translator.getString("EnterPassword"));
        enterButton.setText(translator.getString("EnterButton"));
        noAccountButton.setText(translator.getString("noAccountButton"));
        alertError(currentError);
    }

    private void alertError(String keyString) {
        if (keyString.isEmpty()) {
            errorLabel.setText("");
            currentError = "";
        } else {
            errorLabel.setText(translator.getString(keyString));
            currentError = keyString;
        }
    }

    @Override
    public void switchToThisControllerEvent() {
        changeLanguage();
        loginLine.clear();
        passwordLine.clear();
        alertError("");
    }
}
