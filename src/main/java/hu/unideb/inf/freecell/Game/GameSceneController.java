/*
 * Copyright 2016 University of Debrecen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hu.unideb.inf.freecell.Game;

import hu.unideb.inf.freecell.Model.Card;
import hu.unideb.inf.freecell.Model.Game;
import hu.unideb.inf.freecell.View.CardView;
import hu.unideb.inf.freecell.View.FreeCellView;
import hu.unideb.inf.freecell.View.GameView;
import hu.unideb.inf.freecell.View.HomeCellView;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.*;

/**
 * FXML Controller class
 *
 * @author fanny
 */
public class GameSceneController implements Initializable {
    
    static Logger LOGGER = LoggerFactory.getLogger(GameSceneController.class);
    
    private GameView gameView;
    
    private Game game;
    
    @FXML
    private Pane GamePane;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        game = new Game();
        game.newGame();
        initView();
    }
    
    public void initView() {
        gameView = new GameView();
        
        gameView.setPrefSize(1000, 600);
        gameView.setLayoutX(0);
        gameView.setLayoutY(30);
        
        List<String> s = new ArrayList<>();
        for (List<Card> p : game.tableau) {
            for (Card c : p) {
                s.add(c.getSuit() + "_" + c.getRank());
            }
        }
        
        int it = 0;
        for (int i = 0; i < game.tableau.size(); i++) {
            for (int j = 0; j < game.tableau.get(i).size(); j++) {
                String path = "/images/" + game.tableau.get(i).get(j).getSuit() + "_" + game.tableau.get(i).get(j).getRank() + ".png";
                final CardView cv = new CardView(this.getClass().getResource(path).toString());
                cv.setId(game.tableau.get(i).get(j).getSuit() + "_" + game.tableau.get(i).get(j).getRank());
                
                cv.setOnDragDetected(new onDragDetected(cv));
                cv.setOnDragOver(new onDragOver());
                cv.setOnDragDropped(new onDragDropped(cv));
                cv.setLayoutX(20 + (i * 120));
                cv.setLayoutY(250 + j * 20);
                gameView.getChildren().add(cv);
                
                it++;
            }
        }
        //===============================================================
        //initializing homecells
        List<String> suit = new ArrayList<>();
        for (String s_ : Arrays.asList("clubs", "diamonds", "hearts", "spades")) {
            suit.add(s_);
        }
        
        for (int i = 0; i < 4; i++) {
            final HomeCellView hw = new HomeCellView(this.getClass().getResource("/images/" + suit.get(i) + "_" + game.homeCells.piles.get(suit.get(i)).get(game.homeCells.piles.get(suit.get(i)).size() - 1).getRank() + ".png").toString());
            
            hw.setId(suit.get(i) + "_" + game.homeCells.piles.get(suit.get(i)).get(game.homeCells.piles.get(suit.get(i)).size() - 1).getRank());
            hw.setLayoutX(10 + (i * 120));
            hw.setLayoutY(30);
            
            hw.setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    
                    if (event.getGestureSource().getClass().equals(CardView.class)) {
                        CardView source = (CardView) event.getGestureSource();
                        String[] content = source.getId().split("_");
                        String suit = content[0];
                        int rank = Integer.parseInt(content[1]);
                        Card sourceCard = new Card(suit, rank);
                        content = hw.getId().split("_");
                        suit = content[0];
                        rank = Integer.parseInt(content[1]);
                        Card targetCard = new Card(suit, rank);
                        
                        if (game.addToHomeCell(sourceCard, targetCard)) {
                            
                            hw.setImage(source.getImage());
                            hw.setId(sourceCard.getSuit() + "_" + sourceCard.getRank());
                            
                            gameView.getChildren().remove(source);
                        }
                        
                        event.setDropCompleted(true);
                        
                    } else if (event.getGestureSource().getClass().equals(FreeCellView.class)) {
                        FreeCellView source = (FreeCellView) event.getGestureSource();
                        String[] content = source.getId().split("_");
                        String suit = content[0];
                        int rank = Integer.parseInt(content[1]);
                        Card sourceCard = new Card(suit, rank);
                        content = hw.getId().split("_");
                        suit = content[0];
                        rank = Integer.parseInt(content[1]);
                        Card targetCard = new Card(suit, rank);
                        
                        if (game.addToHomeCell(sourceCard, targetCard)) {
                            
                            hw.setImage(source.getImage());
                            hw.setId(sourceCard.getSuit() + "_" + sourceCard.getRank());
                            
                            source.setImage(source.getBackgroundImage());
                        }
                        event.setDropCompleted(true);
                    }
                    
                    if (game.hasWon()) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Won Window");
                        alert.setHeaderText(null);
                        alert.setContentText("Congratulation! You have won!");
                        
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                try {
                                    Stage stage = (Stage) GamePane.getScene().getWindow();
                                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/StartScene.fxml"));
                                    
                                    Scene scene = new Scene(root);
                                    scene.getStylesheets().add("/styles/Styles.css");
                                    
                                    stage.setTitle("FreeCell");
                                    stage.setScene(scene);
                                    stage.setResizable(false);
                                    stage.centerOnScreen();
                                    stage.show();
                                } catch (IOException ex) {
                                    LOGGER.info(ex.getMessage());
                                }
                            }
                        });
                    }
                    
                    event.consume();
                }
                
            });
            
            hw.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            });
            
            gameView.getChildren().add(hw);
        }

        //========================================================= 
        int numOfFreeCells = game.freeCells.cards.size();
        
        for (int i = 0; i < numOfFreeCells; i++) {
            
            final FreeCellView fv = new FreeCellView(this.getClass().getResource("/images/" + game.freeCells.cards.get(i).getSuit() + "_" + game.freeCells.cards.get(i).getRank() + ".png").toString());
            
            Image image = new Image(this.getClass().getResource("/images/card_background.jpg").toString());
            fv.setBackgroundImage(image);
            fv.setId(game.freeCells.cards.get(i).getSuit() + "_" + game.freeCells.cards.get(i).getRank());
            
            fv.setLayoutX(490 + (i * 120));
            fv.setLayoutY(30);
            
            fv.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            });
            
            fv.setOnDragDropped(new onDragDroppedFreeCell(fv));
            
            fv.setOnDragDetected(new onDragDetectedFreeCell(fv));
            
            gameView.getChildren().add(fv);
        }
        
        for (int i = numOfFreeCells; i < 4; i++) {
            
            final FreeCellView fv = new FreeCellView(this.getClass().getResource("/images/card_background.jpg").toString());
            
            fv.setBackgroundImage(fv.getImage());
            fv.setId("0");
            
            fv.setLayoutX(490 + (i * 120));
            fv.setLayoutY(30);
            
            fv.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            });
            
            fv.setOnDragDropped(new onDragDroppedFreeCell(fv));
            
            fv.setOnDragDetected(new onDragDetectedFreeCell(fv));
            
            gameView.getChildren().add(fv);
        }
        
        GamePane.getChildren().add(gameView);
        
    }
    
    private class onDragDetected implements EventHandler<MouseEvent> {
        
        private final CardView cv;
        
        public onDragDetected(CardView cv) {
            this.cv = cv;
        }
        
        @Override
        public void handle(MouseEvent event) {
            String[] content = cv.getId().split("_");
            String suit = content[0];
            int rank = Integer.parseInt(content[1]);
            Card sourceCard = new Card(suit, rank);
            
            if (game.isLast(sourceCard)) {
                Dragboard db = cv.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent cc = new ClipboardContent();
                Image img = cv.getImage();
                cc.putImage(cv.getImage());
                cc.putString(cv.getId());
                db.setContent(cc);
            } else {
                LOGGER.info("Invalid MOVE: dragged card is not last");
            }
            
            event.consume();
        }
    }
    
    private static class onDragOver implements EventHandler<DragEvent> {
        
        public onDragOver() {
        }
        
        @Override
        public void handle(DragEvent event) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        }
    }
    
    private class onDragDropped implements EventHandler<DragEvent> {
        
        private final CardView cv;
        
        public onDragDropped(CardView cv) {
            this.cv = cv;
        }
        
        @Override
        public void handle(DragEvent event) {
            if (event.getGestureSource().getClass().equals(CardView.class)) {
                CardView source = (CardView) event.getGestureSource();
                String[] content = source.getId().split("_");
                String suit = content[0];
                int rank = Integer.parseInt(content[1]);
                Card sourceCard = new Card(suit, rank);
                content = cv.getId().split("_");
                suit = content[0];
                rank = Integer.parseInt(content[1]);
                Card targetCard = new Card(suit, rank);
                
                if (game.addToTableau(sourceCard, targetCard)) {
                    source.setLayoutX(cv.getLayoutX());
                    source.setLayoutY(cv.getLayoutY() + 20);
                    source.toFront();
                } else {
                }
            } else if (event.getGestureSource().getClass().equals(FreeCellView.class)) {
                FreeCellView source = (FreeCellView) event.getGestureSource();
                String[] content = source.getId().split("_");
                String suit = content[0];
                int rank = Integer.parseInt(content[1]);
                Card sourceCard = new Card(suit, rank);
                content = cv.getId().split("_");
                suit = content[0];
                rank = Integer.parseInt(content[1]);
                Card targetCard = new Card(suit, rank);
                
                if (game.addToTableau(sourceCard, targetCard)) {
                    CardView newCv = new CardView();
                    newCv.setImage(source.getImage());
                    newCv.setId(sourceCard.getSuit() + "_" + sourceCard.getRank());
                    newCv.setFitWidth(100);
                    newCv.setFitHeight(140);
                    newCv.setLayoutX(cv.getLayoutX());
                    newCv.setLayoutY(cv.getLayoutY() + 20);
                    newCv.toFront();
                    newCv.setOnDragDetected(new onDragDetected(newCv));
                    newCv.setOnDragOver(new onDragOver());
                    newCv.setOnDragDropped(new onDragDropped(newCv));
                    
                    gameView.getChildren().add(newCv);
                    
                    source.setImage(source.getBackgroundImage());
                    source.setId("0");
                } else {
                    LOGGER.info("Move is not valid!");
                }
            }
            
            event.consume();
        }
    }
    
    @FXML
    public void onSaveGameMenuAction(ActionEvent event) {
        LOGGER.info("SaveGame pressed!");
        
        TextInputDialog tn = new TextInputDialog();
        tn.setTitle("Saving game");
        tn.setContentText("Please add a name to your game:");
        tn.initStyle(StageStyle.UTILITY);
        
        Optional<String> result = tn.showAndWait();
        if (result.isPresent()) {
            game.saveGame(result.get());
        }
    }
    
    @FXML
    public void onLoadGameMenuAction(ActionEvent event) {
        LOGGER.info("Load game is pressed!");
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        stage.setWidth(400);
        stage.setHeight(300);
        
        AnchorPane p = new AnchorPane();
        p.setPrefSize(400, 300);
        ListView<String> lv = new ListView();
        
        lv.setPrefSize(220, 300);
        ObservableList<String> ob = FXCollections.observableArrayList();
        List<String> savedGames = game.getSavedGames();
        for (String s : savedGames) {
            ob.add(s);
        }
        lv.setItems(ob);
        
        Button button = new Button();
        button.setPrefSize(100, 30);
        button.setText("LoadGame");
        button.setLayoutX(260);
        button.setLayoutY(100);
        
        button.setOnAction((ActionEvent event1) -> {
            game = new Game();
            gameView.getChildren().clear();
            game.loadGame(lv.getSelectionModel().getSelectedItem());
            initView();
        });
        
        p.getChildren().add(lv);
        p.getChildren().add(button);
        Scene sc = new Scene(p);
        stage.setScene(sc);
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
        
    }
    
    @FXML
    public void onNewGameMenuAction(ActionEvent event) {
        gameView.getChildren().clear();
        gameView = new GameView();
        
        game = new Game();
        game.newGame();
        initView();
    }
    
    private class onDragDroppedFreeCell implements EventHandler<DragEvent> {
        
        private final FreeCellView fv;
        
        public onDragDroppedFreeCell(FreeCellView fv) {
            this.fv = fv;
        }
        
        @Override
        public void handle(DragEvent event) {
            
            if (event.getGestureSource().getClass().equals(CardView.class)) {
                CardView source = (CardView) event.getGestureSource();
                String[] content = source.getId().split("_");
                String suit = content[0];
                int rank = Integer.parseInt(content[1]);
                Card sourceCard = new Card(suit, rank);
                
                if (game.addToFreeCell(sourceCard)) {
                    fv.setImage(source.getImage());
                    fv.setId(sourceCard.getSuit() + "_" + sourceCard.getRank());
                    
                    gameView.getChildren().remove(source);
                }
                
                event.setDropCompleted(true);
                
                event.consume();
            }
        }
    }
    
    private static class onDragDetectedFreeCell implements EventHandler<MouseEvent> {
        
        private final FreeCellView fv;
        
        public onDragDetectedFreeCell(FreeCellView fv) {
            this.fv = fv;
        }
        
        @Override
        public void handle(MouseEvent event) {
            if (!fv.getId().equals("0")) {
                Dragboard db = fv.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent cc = new ClipboardContent();
                cc.putImage(fv.getImage());
                db.setContent(cc);
                
                event.consume();
            }
        }
    }
    
}
