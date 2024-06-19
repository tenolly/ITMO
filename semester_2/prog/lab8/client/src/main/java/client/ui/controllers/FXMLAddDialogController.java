package client.ui.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle;

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
import shared.request.RequestStatus;

public class FXMLAddDialogController implements MultiLang, Switchable  {
    @FXML private Label nameLabel;
    @FXML private Label xLabel;
    @FXML private Label yLabel;
    @FXML private Label enginePowerLabel;
    @FXML private Label numberOfWheelsLabel;
    @FXML private Label fuelConsumptionLabel;
    @FXML private Label fuelTypeLabel;
    @FXML private Label errorLabel;
    @FXML private TextField nameLine;
    @FXML private TextField xLine;
    @FXML private TextField yLine;
    @FXML private TextField enginePowerLine;
    @FXML private TextField numberOfWheelsLine;
    @FXML private TextField fuelConsumptionLine;
    @FXML private ComboBox<String> fuelTypeComboBox;
    @FXML private ComboBox<String> langComboBox;
    @FXML private Button okButton;
    @FXML private Button cancelButton;

    private String currentError = "";

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
                HashMap<String, Object> args = new HashMap<String, Object>();
                args.put("command", "add");
                args.put("name", nameLine.getText());
                args.put("x", xLine.getText());
                args.put("y", yLine.getText());
                args.put("enginePower", enginePowerLine.getText());
                args.put("numberOfWheels", numberOfWheelsLine.getText());
                args.put("fuelConsumption", fuelConsumptionLine.getText());
                args.put("fuelType", fuelTypeComboBox.getValue());

                try {
                    var response = ClientSession.makeRequest("cmd", args);
                    if (response.getStatus().equals(RequestStatus.FULLFILED)) {
                        Stage stage = (Stage) okButton.getScene().getWindow();
                        stage.close();
                    } else {
                        alertError(response.getResponse());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    alertError("ServerIsUnavailableError");
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

    @Override
    public void switchToThisControllerEvent() {
        changeLanguage();
        nameLine.clear();
        xLine.clear();
        yLine.clear();
        enginePowerLine.clear();
        numberOfWheelsLine.clear();
        fuelConsumptionLine.clear();
        alertError("");
    }

    @Override
    public void changeLanguage() {
        langComboBox.setValue(ClientSession.getCurrentLocale());
        nameLabel.setText(translator.getString("name"));
        xLabel.setText(translator.getString("x"));
        yLabel.setText(translator.getString("y"));
        enginePowerLabel.setText(translator.getString("enginePower"));
        numberOfWheelsLabel.setText(translator.getString("numberOfWheels"));
        fuelConsumptionLabel.setText(translator.getString("fuelConsumption"));
        fuelTypeLabel.setText(translator.getString("fuelType"));
        xLine.setPromptText(translator.getString("columnXPromprt"));
        yLine.setPromptText(translator.getString("columnYPromprt"));
        enginePowerLine.setPromptText(translator.getString("columnEnginePowerPrompt"));
        numberOfWheelsLine.setPromptText(translator.getString("columnNumberOfWheelsPrompt"));
        fuelConsumptionLine.setPromptText(translator.getString("columnFuelConsumptionPrompt"));
        okButton.setText(translator.getString("okButton"));
        cancelButton.setText(translator.getString("cancelButton"));
        alertError(currentError);
    }

    private void alertError(String error) {
        currentError = error;
        errorLabel.setText(translator.getStringOrReturnItBack(currentError));
    }
}
