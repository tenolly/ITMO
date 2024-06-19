package client.ui.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.ResourceBundle; 

import client.ClientSession;
import client.ui.components.ComboBoxItem;
import client.ui.controllers.interfaces.MultiLang;
import client.ui.controllers.interfaces.Switchable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import shared.request.RequestStatus;

public class FXMLUpdateDialogController implements Switchable, MultiLang {
    @FXML private TextField valueLine;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    @FXML private ComboBox<String> langComboBox;
    @FXML private ComboBox<ComboBoxItem> fieldComboBox;
    @FXML private Label errorLabel;

    private String currentError = "";
    private long vehicleId;

    @FXML
    public void initialize() {
        fieldComboBox.setItems(FXCollections.observableArrayList(
            new ComboBoxItem("name", "name"),
            new ComboBoxItem("x", "x"),
            new ComboBoxItem("y", "y"),
            new ComboBoxItem("enginePower", "enginePower"),
            new ComboBoxItem("numberOfWheels", "numberOfWheels"),
            new ComboBoxItem("fuelConsumption", "fuelConsumption"),
            new ComboBoxItem("fuelType", "fuelType")
        ));

        langComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            translator.setBundle(ResourceBundle.getBundle("locales/lang", localesMap.get(newValue)));
            ClientSession.setCurrentLocale(newValue);
            changeLanguage();
        });

        configureComboBox();

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    HashMap<String, Object> args = new HashMap<String, Object>();
                    args.put("command", "update");
                    args.put("vehicle_id", Long.toString(vehicleId));
                    args.put("field", fieldComboBox.getValue().getId());
                    
                    if (valueLine.getText().isEmpty()) args.put("value", null);
                    else args.put("value", valueLine.getText());

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

    public void setVehicleId(long vehicleId) {
        this.vehicleId = vehicleId;
    }

    private void configureComboBox() {
        fieldComboBox.setCellFactory(param -> new ListCell<ComboBoxItem>() {
            @Override
            protected void updateItem(ComboBoxItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getValue());
            }
        });

        fieldComboBox.setButtonCell(new ListCell<ComboBoxItem>() {
            @Override
            protected void updateItem(ComboBoxItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getValue());
            }
        });
    }

    @Override
    public void changeLanguage() {
        langComboBox.setValue(ClientSession.getCurrentLocale());
        okButton.setText(translator.getString("okButton"));
        valueLine.setPromptText(translator.getString("InputNewValue"));
        cancelButton.setText(translator.getString("cancelButton"));
        
        for (ComboBoxItem item : fieldComboBox.getItems()) {
            item.setValue(translator.getString(item.getId()));
        }
        configureComboBox();

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
