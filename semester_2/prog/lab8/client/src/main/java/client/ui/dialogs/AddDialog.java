package client.ui.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import client.ui.controllers.FXMLAddDialogController;

public class AddDialog {
    private Stage dialogStage;
    private FXMLAddDialogController controller;

    public AddDialog(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/add_dialog.fxml"));
        Parent root = loader.load();

        controller = loader.getController();
        controller.changeLanguage();
        controller.switchToThisControllerEvent();

        dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        dialogStage.setScene(new Scene(root));
        dialogStage.setTitle("Add Dialog");
        dialogStage.setResizable(false);
    }

    public void showAndWait() {
        dialogStage.showAndWait();
    }

    public FXMLAddDialogController getController() {
        return controller;
    }
}
