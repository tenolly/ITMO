package client.ui.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import client.ui.controllers.FXMLUpdateDialogController;

public class UpdateDialog {
    private Stage dialogStage;
    private FXMLUpdateDialogController controller;

    public UpdateDialog(Stage primaryStage, Long vehicleId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/update_dialog.fxml"));
        Parent root = loader.load();

        controller = loader.getController();
        controller.setVehicleId(vehicleId);
        controller.changeLanguage();
        controller.switchToThisControllerEvent();

        dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        dialogStage.setScene(new Scene(root));
        dialogStage.setTitle("Update Dialog");
        dialogStage.setResizable(false);
    }

    public void showAndWait() {
        dialogStage.showAndWait();
    }

    public FXMLUpdateDialogController getController() {
        return controller;
    }
}
