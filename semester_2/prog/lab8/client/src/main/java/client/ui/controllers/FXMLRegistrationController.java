package client.ui.controllers;

import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import client.ScenesName;
import client.ui.controllers.interfaces.MultiLang;
import client.ui.controllers.interfaces.Switchable;
import client.ClientSession;
import client.SceneSwitcher;
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

public class FXMLRegistrationController implements MultiLang, Switchable {
    @FXML private Label registrationLabel;
    @FXML private TextField loginLine;
    @FXML private PasswordField passwordLine;
    @FXML private PasswordField passwordLine2;
    @FXML private Button registrationButton;
    @FXML private Button haveAccountButton;
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

        loginLine.textProperty().addListener((observableValue, oldValue, newValue) -> {
            verifyLogin(newValue);
        });

        passwordLine.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (verifyPassword(newValue)) {
                verifyPassword2(passwordLine2.getText());
            }
        });

        passwordLine2.textProperty().addListener((observableValue, oldValue, newValue) -> {
            verifyPassword2(newValue);
        });

        registrationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String loginValue = loginLine.getText();
                String passwordValue = passwordLine.getText();
                String passwordValue2 = passwordLine2.getText();

                if (verifyLogin(loginValue) && verifyPassword(passwordValue) && verifyPassword2(passwordValue2)) {
                    HashMap<String, Object> args = new HashMap<String, Object>();
                    args.put("command", "register");

                    try {
                        ClientSession.setUser(new User(loginValue, passwordValue));
                        var response = ClientSession.makeRequest("auth", args);
                        if (response.getStatus().equals(RequestStatus.FULLFILED)) {
                            SceneSwitcher.switchTo(ScenesName.ENTER);
                        } else {
                            alertError("UserAlreadyExists");
                        }
                    } catch (Exception e) {
                        alertError("ServerIsUnavailableError");
                        e.printStackTrace();
                    }
                }

                ClientSession.unsetUser();
            }
        });

        haveAccountButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SceneSwitcher.switchTo(ScenesName.ENTER);
            }
        });
    }

    private boolean verifyLogin(String login) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]+$");

        if (!pattern.matcher(login).matches() && !login.isEmpty()) {
            alertError("IncorrectLogin");
            return false;
        } else if (login.length() > 12) {
            alertError("TooLargeLogin");
            return false;
        } else if (login.length() < 4) {
            alertError("TooSmallLogin");
            return false;
        } else {
            alertError("");
            return true;
        }
    }

    private boolean verifyPassword(String password) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]+$");

        if (!pattern.matcher(password).matches() && !password.isEmpty()) {
            alertError("IncorrectPassword");
            return false;
        } else if (password.length() > 16) {
            alertError("TooLargePassword");
            return false;
        } else if (password.length() < 6) {
            alertError("TooSmallPassword");
            return false;
        } else {
            alertError("");
            return true;
        }
    }

    private boolean verifyPassword2(String password) {
        if (!password.equals(passwordLine.getText())) {
            alertError("DifferentPasswords");
            return false;
        } else {
            alertError("");
            return true;
        }
    }

    public void changeLanguage() {
        langComboBox.setValue(ClientSession.getCurrentLocale());
        registrationLabel.setText(translator.getString("Registration"));
        loginLine.setPromptText(translator.getString("EnterLogin"));
        passwordLine.setPromptText(translator.getString("EnterPassword"));
        passwordLine2.setPromptText(translator.getString("EnterPasswordAgain"));
        registrationButton.setText(translator.getString("RegisterButton"));
        haveAccountButton.setText(translator.getString("AlreadyHaveAccountButton"));
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
        passwordLine2.clear();
        alertError("");
    }
}
