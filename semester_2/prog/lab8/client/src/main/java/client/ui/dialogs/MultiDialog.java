package client.ui.dialogs;

import java.io.IOException;

import com.google.common.base.Function;

import client.ui.controllers.FXMLMultiDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MultiDialog {
    private Stage dialogStage;
    private FXMLMultiDialogController controller;

    public MultiDialog(Stage primaryStage, String promptKey, Function<String, String> action) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/multi_dialog.fxml"));
        Parent root = loader.load();

        controller = loader.getController();
        controller.setPromptKey(promptKey);
        controller.setAction(action);
        controller.changeLanguage();
        controller.switchToThisControllerEvent();

        dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        dialogStage.setScene(new Scene(root));
        dialogStage.setTitle("Dialog");
        dialogStage.setResizable(false);
    }

    public void showAndWait() {
        dialogStage.showAndWait();
    }

    public FXMLMultiDialogController getController() {
        return controller;
    }
}
