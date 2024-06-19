package client.ui.controllers;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import client.ClientSession;
import client.SceneSwitcher;
import client.ScenesName;
import client.managers.execptions.RecursionFoundException;
import client.ui.controllers.interfaces.MultiLang;
import client.ui.controllers.interfaces.Switchable;
import client.ui.dialogs.AddDialog;
import client.ui.dialogs.MultiDialog;
import client.ui.dialogs.UpdateDialog;
import client.utils.ColorUtil;
import client.utils.CommandUtil;
import client.utils.TableUtil;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Shape;
import javafx.application.Platform;
import shared.database.Vehicle;

public class FXMLMainWindowController implements MultiLang, Switchable {
    @FXML private Pane visual;
    @FXML private ComboBox<String> langComboBox;
    @FXML private Label usernameLine;
    @FXML private Button logOutButton;
    @FXML private Button showHideNameButton;
    @FXML private TextArea consoleTextArea;
    @FXML private TableView<Vehicle> table;
    @FXML private TableColumn<Vehicle, Long> columnId;
    @FXML private TableColumn<Vehicle, String> columnName;
    @FXML private TableColumn<Vehicle, Double> columnX;
    @FXML private TableColumn<Vehicle, Float> columnY;
    @FXML private TableColumn<Vehicle, String> columnCreationDate;
    @FXML private TableColumn<Vehicle, Integer> columnNumberOfWheels;
    @FXML private TableColumn<Vehicle, Float> columnFuelConsumption;
    @FXML private TableColumn<Vehicle, String> columnFuelType;
    @FXML private TableColumn<Vehicle, String> columnCreatedBy;
    @FXML private TableColumn<Vehicle, Float> columnEnginePower;
    @FXML private Button addCommand;
    @FXML private Button removeCommand;
    @FXML private Button removeFirstCommand;
    @FXML private Button removeHeadCommand;
    @FXML private Button removeLowerCommand;
    @FXML private Button filterStartsWithNameCommand;
    @FXML private Button filterGreaterFuelConsumptionCommand;
    @FXML private Button countLessThanEnginePowerCommand;
    @FXML private Button clearCommand; 
    @FXML private Button helpCommand;
    @FXML private Button infoCommand;
    @FXML private Button showCommand;
    @FXML private Button executeScriptCommand;

    private boolean isShownNickname = true;
    private String textAreaCache = "";

    private Long lastTimeUpdate = System.currentTimeMillis();
    private HashMap<String, Object> lastShowArgs;

    private Thread refresher = new Thread() {
        @SuppressWarnings("unchecked")
        @Override
        public void run(){
            while (true) {
                try {
                    Thread.sleep(3_000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if (System.currentTimeMillis() - lastTimeUpdate > 3_000 && lastShowArgs != null) {
                    Platform.runLater(() -> {
                        try {
                            var response = ClientSession.makeRequest("cmd", lastShowArgs);
                            showVehicles((ArrayList<Vehicle>) response.getSharedData());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        lastTimeUpdate = System.currentTimeMillis();
                    });
                }
            }
        }
    };

    @SuppressWarnings("unchecked")
    @FXML
    void initialize() {
        refresher.start();

        columnId.setCellValueFactory(vehicle -> new SimpleLongProperty(vehicle.getValue().getId()).asObject());
        columnName.setCellValueFactory(vehicle -> new SimpleStringProperty(vehicle.getValue().getName()));
        columnX.setCellValueFactory(vehicle -> new SimpleDoubleProperty(vehicle.getValue().getCoordinates().getX()).asObject());
        columnY.setCellValueFactory(vehicle -> new SimpleFloatProperty(vehicle.getValue().getCoordinates().getY()).asObject());
        columnCreationDate.setCellValueFactory(vehicle -> new SimpleStringProperty(translator.formatDateTime(vehicle.getValue().getCreationDate())));
        columnNumberOfWheels.setCellValueFactory(vehicle -> new SimpleIntegerProperty(vehicle.getValue().getNumberOfWheels()).asObject());
        columnFuelConsumption.setCellValueFactory(vehicle -> new SimpleFloatProperty(vehicle.getValue().getFuelConsumption()).asObject());
        columnFuelType.setCellValueFactory(vehicle -> new SimpleStringProperty(vehicle.getValue().getFuelType().toString()));
        columnCreatedBy.setCellValueFactory(vehicle -> new SimpleStringProperty(vehicle.getValue().getUsername()));
        columnEnginePower.setCellValueFactory(vehicle -> new SimpleFloatProperty(vehicle.getValue().getEnginePower()).asObject());

        langComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            translator.setBundle(ResourceBundle.getBundle("locales/lang", localesMap.get(newValue)));
            ClientSession.setCurrentLocale(newValue);
            changeLanguage();
        });

        addCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage primaryStage = (Stage) addCommand.getScene().getWindow();
                    AddDialog addDialog = new AddDialog(primaryStage);
                    addDialog.showAndWait();
                    showAll();
                    changeLanguage();
                    outputMessage("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        removeFirstCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HashMap<String, Object> args = new HashMap<String, Object>();
                args.put("command", "remove_first");
                try {
                    var response = ClientSession.makeRequest("cmd", args);
                    showAll();
                    outputMessage(response.getResponse());
                } catch (IOException e) {
                    outputMessage("ServerIsUnavailableError");
                    e.printStackTrace();
                }
            }
        });

        removeHeadCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HashMap<String, Object> args = new HashMap<String, Object>();
                args.put("command", "remove_head");
                try {
                    var response = ClientSession.makeRequest("cmd", args);
                    showAll();
                    outputMessage(response.getResponse());
                } catch (IOException e) {
                    outputMessage("ServerIsUnavailableError");
                    e.printStackTrace();
                }
            }
        });

        removeCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage primaryStage = (Stage) addCommand.getScene().getWindow();
                    MultiDialog multiDialog = new MultiDialog(primaryStage, "enterIdValue", value -> {
                        HashMap<String, Object> args = new HashMap<String, Object>();
                        args.put("command", "remove_by_id");
                        args.put("id", value);

                        try {
                            var response = ClientSession.makeRequest("cmd", args);
                            outputMessage(response.getResponse());
                            showAll();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "ServerIsUnavailableError";
                        }

                        return "";
                    });
                    multiDialog.showAndWait();
                    showAll();
                    changeLanguage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        removeLowerCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage primaryStage = (Stage) addCommand.getScene().getWindow();
                    MultiDialog multiDialog = new MultiDialog(primaryStage, "enterIdValue", value -> {
                        HashMap<String, Object> args = new HashMap<String, Object>();
                        args.put("command", "remove_lower");
                        args.put("id", value);

                        try {
                            var response = ClientSession.makeRequest("cmd", args);
                            outputMessage(response.getResponse());
                            showAll();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "ServerIsUnavailableError";
                        }

                        return "";
                    });
                    multiDialog.showAndWait();
                    showAll();
                    changeLanguage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        filterStartsWithNameCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage primaryStage = (Stage) addCommand.getScene().getWindow();
                    MultiDialog multiDialog = new MultiDialog(primaryStage, "enterSubstring", value -> {
                        HashMap<String, Object> args = new HashMap<String, Object>();
                        args.put("command", "filter_starts_with_name");
                        args.put("name", value);

                        try {
                            var response = ClientSession.makeRequest("cmd", args);
                            outputMessage(response.getResponse());
                            lastTimeUpdate = System.currentTimeMillis();
                            lastShowArgs = args;
                            showVehicles((ArrayList<Vehicle>) response.getSharedData());
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "ServerIsUnavailableError";
                        }

                        return "";
                    });
                    multiDialog.showAndWait();
                    changeLanguage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        filterGreaterFuelConsumptionCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage primaryStage = (Stage) addCommand.getScene().getWindow();
                    MultiDialog multiDialog = new MultiDialog(primaryStage, "enterFuelConsumption", value -> {
                        HashMap<String, Object> args = new HashMap<String, Object>();
                        args.put("command", "filter_greater_than_fuel_consumption");
                        args.put("fuelConsumption", value);

                        try {
                            var response = ClientSession.makeRequest("cmd", args);
                            outputMessage(response.getResponse());
                            lastTimeUpdate = System.currentTimeMillis();
                            lastShowArgs = args;
                            showVehicles((ArrayList<Vehicle>) response.getSharedData());
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "ServerIsUnavailableError";
                        }

                        return "";
                    });
                    multiDialog.showAndWait();
                    changeLanguage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        countLessThanEnginePowerCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage primaryStage = (Stage) addCommand.getScene().getWindow();
                    MultiDialog multiDialog = new MultiDialog(primaryStage, "enterEnginePower", value -> {
                        HashMap<String, Object> args = new HashMap<String, Object>();
                        args.put("command", "count_less_than_engine_power");
                        args.put("enginePower", value);

                        try {
                            var response = ClientSession.makeRequest("cmd", args);
                            outputMessage(response.getResponse());
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "ServerIsUnavailableError";
                        }

                        return "";
                    });
                    multiDialog.showAndWait();
                    changeLanguage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        clearCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HashMap<String, Object> args = new HashMap<String, Object>();
                args.put("command", "clear");
                try {
                    var response = ClientSession.makeRequest("cmd", args);
                    showAll();
                    outputMessage(response.getResponse());
                } catch (IOException e) {
                    outputMessage("ServerIsUnavailableError");
                    e.printStackTrace();
                }
            }
        });

        helpCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                outputMessage("Help");
            }
        });

        infoCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HashMap<String, Object> args = new HashMap<String, Object>();
                args.put("command", "info");

                try {
                    var response = ClientSession.makeRequest("cmd", args);
                    String[] messageData = response.getResponse().split("\\|");
                    outputMessage(MessageFormat.format(
                        translator.getString("Info"), 
                        messageData[0], messageData[1], messageData[2]
                    ));
                } catch (IOException e) {
                    outputMessage("ServerIsUnavailableError");
                    e.printStackTrace();
                }
            }
        });

        executeScriptCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FileChooser fileChooser = new FileChooser();
                    File selectedFile = fileChooser.showOpenDialog((Stage) executeScriptCommand.getScene().getWindow());
                    ArrayList<String> list = ClientSession.getClientEnviroment().getListOfLines(
                        selectedFile.getAbsolutePath(), (string) -> string.startsWith("execute_script"));

                    for (String string : list) {
                        ArrayList<String> parts = new ArrayList<String>(Arrays.asList(string.trim().split("\\s+")));
                        String cmd = parts.get(0);
                        parts.remove(0);

                        try {
                            ClientSession.makeRequest("cmd", CommandUtil.makeArgs(cmd, parts));
                        } catch (IOException e) {
                            outputMessage("ServerIsUnavailableError");
                            e.printStackTrace();
                            break;
                        }
                    }
                } catch (RecursionFoundException e) {
                    outputMessage("RecursionFoundException");
                } catch (IOException e) {
                    outputMessage("IOException");
                }
                
                showAll();
            }
        });

        showCommand.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showAll();
                outputMessage("");
            }
        });

        table.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1 && table.getSelectionModel().getSelectedItem() != null) {
                Vehicle selectedVehicle = table.getSelectionModel().getSelectedItem();
                if (!selectedVehicle.getUsername().equals(ClientSession.getUser().getUsername())) {
                    outputMessage("InaccessibleObject");
                    return;
                }
                try {
                    Stage primaryStage = (Stage) table.getScene().getWindow();
                    UpdateDialog updateDialog = new UpdateDialog(primaryStage, selectedVehicle.getId());
                    updateDialog.showAndWait();
                    showAll();
                    changeLanguage();
                    outputMessage("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        showHideNameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isShownNickname) {
                    showHideNameButton.setText(translator.getString("ShowUser"));
                    usernameLine.setText("");
                    isShownNickname = false;
                } else {
                    showHideNameButton.setText(translator.getString("HideUser"));
                    setCurrentUsername();
                    isShownNickname = true;
                }
            }
        });

        logOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ClientSession.unsetUser();
                SceneSwitcher.switchTo(ScenesName.ENTER);
            }
        });
    }

    public void setCurrentUsername() {
        usernameLine.setText(ClientSession.getUser().getUsername());
    }

    public void clearCanvas() {
        visual.getChildren().clear();
    }

    public void makeAnimation(Rectangle shape) {
        shape.setX(shape.getX() - 150);
        shape.setOpacity(0);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), 
            new KeyValue(shape.translateXProperty(), 150), 
            new KeyValue(shape.opacityProperty(), 1)
        ));

        timeline.play();
    }

    public void makeAnimation(Circle shape) {
        shape.setCenterX(shape.getCenterX() - 150);
        shape.setOpacity(0);
        
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), 
            new KeyValue(shape.translateXProperty(), 150), 
            new KeyValue(shape.opacityProperty(), 1)
        ));

        timeline.play();
    }

    public void makeAnimation(Polygon shape) {
        shape.setLayoutX(shape.getLayoutX() - 150);
        shape.setOpacity(0);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), 
            new KeyValue(shape.translateXProperty(), 150), 
            new KeyValue(shape.opacityProperty(), 1)
        ));

        timeline.play();
    }

    public void drawVehicle(Vehicle vehicle, boolean animate) {
        var rgb = ColorUtil.toColor(vehicle.getUsername());

        var vehicleColor = Color.color(rgb[0], rgb[1], rgb[2]);
        var vehicleX = Math.min(visual.getPrefWidth() - 50, (vehicle.getCoordinates().getX() + 830) / 10);
        var vehicleY = Math.min(visual.getPrefHeight() - 50, (vehicle.getCoordinates().getY() + 450) / 10);
        var vehicleSize = Math.min(vehicle.getEnginePower() / 150, 1) * 40 + 10;
        var numberOfWheels = Math.min(vehicle.getNumberOfWheels(), 30);
        
        Shape fig = new Rectangle();  // pass
        switch (vehicle.getFuelType()) {
            case KEROSENE:
                fig = drawSquare(vehicleColor, vehicleX, vehicleY, vehicleSize);
                if (animate) makeAnimation((Rectangle) fig);
                break;
            case NUCLEAR:
                fig = drawCircle(vehicleColor, vehicleX, vehicleY, vehicleSize / 2);
                if (animate) makeAnimation((Circle) fig);
                break;
            case ANTIMATTER:
                fig = drawTriangle(vehicleColor, vehicleX, vehicleY, vehicleSize);
                if (animate) makeAnimation((Polygon) fig);
                break;
        }

        EventHandler<MouseEvent> mouseHandler = (MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                if (!vehicle.getUsername().equals(ClientSession.getUser().getUsername())) {
                    outputMessage("InaccessibleObject");
                    return;
                }
                try {
                    Stage primaryStage = (Stage) table.getScene().getWindow();
                    UpdateDialog updateDialog = new UpdateDialog(primaryStage, vehicle.getId());
                    updateDialog.showAndWait();
                    showAll();
                    changeLanguage();
                    outputMessage("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                outputMessage(vehicle.toString());
            }
        };

        fig.setOnMouseClicked(mouseHandler);
        for (Circle wheel : drawWheels(vehicleColor, vehicleX, vehicleY, vehicleSize, numberOfWheels)) {
            if (animate) makeAnimation(wheel);
            wheel.setOnMouseClicked(mouseHandler);
        };
    }

    public Rectangle drawSquare(Color color, double centerX, double centerY, double size) {
        Rectangle square = new Rectangle(centerX - (size / 2), centerY - (size / 2), size, size);
        square.setFill(color);

        visual.getChildren().add(square);
        return square;
    }

    public Polygon drawTriangle(Color color, double centerX, double centerY, double size) {
        double height = (Math.sqrt(3) / 2) * size;

        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
            centerX, centerY - height / 2,
            centerX - size / 2, centerY + height / 2,
            centerX + size / 2, centerY + height / 2
        });

        polygon.setFill(color);
        visual.getChildren().add(polygon);
        return polygon;
    }
    
    public Circle drawCircle(Color color, double centerX, double centerY, double radius) {
        Circle circle = new Circle(centerX, centerY, radius, color);
        visual.getChildren().add(circle);
        return circle;
    }

    public ArrayList<Circle> drawWheels(Color color, double centerX, double centerY, double vehicleSize, int numberOfWheels) {
        double wheelRadius = vehicleSize / 6;
        double y = centerY + vehicleSize / 2 + wheelRadius;
        
        ArrayList<Circle> list = new ArrayList<Circle>();
        for (int i = 0; i < numberOfWheels; i++) {
            double x = centerX - (vehicleSize / 2) + (i * (vehicleSize / (numberOfWheels - 1)));
            
            if (numberOfWheels == 1) {
                x = centerX;
            }

            list.add(drawCircle(color, x, y, wheelRadius));
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public void showAll() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("command", "show");

        try {
            var response = ClientSession.makeRequest("cmd", args);
            lastTimeUpdate = System.currentTimeMillis();
            lastShowArgs = args;
            showVehicles((ArrayList<Vehicle>) response.getSharedData());
        } catch (IOException e) {
            outputMessage("ServerIsUnavailableError");
            e.printStackTrace();
        }
    }

    public void showVehicles(ArrayList<Vehicle> array) {
        clearCanvas();
        for (Vehicle vehicle : array) {
            if (table.getItems().contains(vehicle)) {
                drawVehicle(vehicle, false);
            } else {
                drawVehicle(vehicle, true);
            }
        }
        table.getItems().clear();
        table.getItems().addAll(array);
        TableUtil.autoResizeColumns(table);
    }

    @Override
    public void changeLanguage() {
        langComboBox.setValue(ClientSession.getCurrentLocale());
        if (isShownNickname) {
            showHideNameButton.setText(translator.getString("HideUser"));
        } else {
            showHideNameButton.setText(translator.getString("ShowUser"));
        }
        logOutButton.setText(translator.getString("LogoutButton"));
        outputMessage(textAreaCache);

        columnId.setText(translator.getString("id"));
        columnName.setText(translator.getString("name"));
        columnX.setText(translator.getString("x"));
        columnY.setText(translator.getString("y"));
        columnCreationDate.setText(translator.getString("creationDate"));
        columnNumberOfWheels.setText(translator.getString("numberOfWheels"));
        columnFuelConsumption.setText(translator.getString("fuelConsumption"));
        columnFuelType.setText(translator.getString("fuelType"));
        columnCreatedBy.setText(translator.getString("createdBy"));
        columnEnginePower.setText(translator.getString("enginePower"));

        addCommand.setText(translator.getString("addCommand"));
        removeCommand.setText(translator.getString("removeCommand"));
        removeFirstCommand.setText(translator.getString("removeFirstCommand"));
        removeHeadCommand.setText(translator.getString("removeHeadCommand"));
        removeLowerCommand.setText(translator.getString("removeLowerCommand"));
        filterStartsWithNameCommand.setText(translator.getString("filterStartsWithNameCommand"));
        filterGreaterFuelConsumptionCommand.setText(translator.getString("filterGreaterFuelConsumptionCommand"));
        countLessThanEnginePowerCommand.setText(translator.getString("countLessThanEnginePowerCommand"));
        clearCommand.setText(translator.getString("clearCommand")); 
        helpCommand.setText(translator.getString("helpCommand"));
        infoCommand.setText(translator.getString("infoCommand"));
        showCommand.setText(translator.getString("showCommand"));
        executeScriptCommand.setText(translator.getString("executeScriptCommand"));

        consoleTextArea.setText(translator.getStringOrReturnItBack(textAreaCache));
    }

    @Override
    public void switchToThisControllerEvent() {
        var rgb = ColorUtil.toColor(ClientSession.getUser().getUsername());
        usernameLine.setTextFill(Color.color(rgb[0], rgb[1], rgb[2]));

        isShownNickname = true;
        outputMessage("");
        setCurrentUsername();
        changeLanguage();
        showAll();
    }

    private void outputMessage(String message) {
        consoleTextArea.setText(translator.getStringOrReturnItBack(message));
        textAreaCache = message;
    }
}
