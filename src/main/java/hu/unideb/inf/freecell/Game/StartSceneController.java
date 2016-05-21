package hu.unideb.inf.freecell.Game;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class StartSceneController implements Initializable {

    @FXML
    private Button StartSceneNewGameButton;

    @FXML
    private Button StartSceneHighscoreButton;

    @FXML
    private Button StartSceneExitButton;

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;

        if (event.getSource() == StartSceneNewGameButton) {
            stage = (Stage) StartSceneNewGameButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/fxml/GameScene.fxml"));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } else if (event.getSource() == StartSceneExitButton) {
            stage = (Stage) StartSceneNewGameButton.getScene().getWindow();
            stage.close();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
