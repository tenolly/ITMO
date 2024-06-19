package client.ui.controllers;

import java.util.ResourceBundle;

import com.google.common.base.Function;

import client.ClientSession;
import client.ui.controllers.interfaces.MultiLang;
import client.ui.controllers.interfaces.Switchable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLMultiDialogController implements Switchable, MultiLang {
    @FXML private TextField valueLine;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    @FXML private ComboBox<String> langComboBox;
    @FXML private Label errorLabel;

    private String promptKey = "";
    private String currentError = "";

    private Function<String, String> action;
    private String actionError;

    @FXML
    public void initialize() {
        langComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            translator.setBundle(ResourceBundle.getBundle("locales/lang", localesMap.get(newValue)));
            ClientSession.setCurrentLocale(newValue);
            changeLanguage();
        });

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (valueLine.getText().isEmpty()) actionError = action.apply(null);
                    else actionError = action.apply(valueLine.getText());
                    if (actionError.isEmpty()) {
                        Stage stage = (Stage) cancelButton.getScene().getWindow();
                        stage.close();
                    } else {
                        alertError(actionError);
                    }
                } catch (Exception e) {
                    alertError("Failed");
                    e.printStackTrace();
                }
                
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            }
        });
    }

    public void setPromptKey(String promptKey) {
        this.promptKey = promptKey;
    }

    public void setAction(Function<String, String> action) {
        this.action = action;
    }

    @Override
    public void changeLanguage() {
        langComboBox.setValue(ClientSession.getCurrentLocale());
        valueLine.setPromptText(translator.getStringOrReturnItBack(promptKey));
        okButton.setText(translator.getString("okButton"));
        cancelButton.setText(translator.getString("cancelButton"));
        alertError(currentError);
    }

    @Override
    public void switchToThisControllerEvent() {
        changeLanguage();
        valueLine.clear();
        alertError("");
    }

    private void alertError(String error) {
        currentError = error;
        errorLabel.setText(translator.getStringOrReturnItBack(currentError));
    }
}
