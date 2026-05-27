package com.example.samplejavafxproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.io.InputStream;

public class HelloController {
    @FXML private VBox screen;

    @FXML private Pane pane1;
    @FXML private Pane pane2;
    @FXML private Pane pane3;
    @FXML private Pane pane4;

    @FXML private Pane bPane;
    private StackPane blocker;

    private Player p1;
    private Player p2;
    private Player p3;
    private Player p4;

    private boolean turn = false;

    @FXML public void initialize() {
        Image img = new Image(getClass().getResourceAsStream("/chopstick.png"));

        p1 = new Player(pane1, img, false);
        p2 = new Player(pane2, img, false);
        p3 = new Player(pane3, img, true);
        p4 = new Player(pane4, img, true);

        Timeline timeline = new Timeline();

        KeyFrame keyframe = new KeyFrame(Duration.millis(1000.0/60.0), event -> {
            if ((p1.getScore() == 5 && p2.getScore() == 5) || (p3.getScore() == 5 && p4.getScore() == 5)) {
                if (blocker == null) {
                    blocker = new StackPane();
                    blocker.prefWidthProperty().bind(bPane.widthProperty());
                    blocker.prefHeightProperty().bind(bPane.heightProperty());
                    blocker.setStyle("-fx-background-color: white;");

                    Text endText = new Text("Game Over!");
                    blocker.getChildren().add(endText);
                    bPane.getChildren().add(blocker);
                    endText.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");
                    endText.applyCss();
                    double textW = endText.getBoundsInLocal().getWidth();
                    double textH = endText.getBoundsInLocal().getHeight();
                    endText.setViewOrder(-1);

                    bPane.setMouseTransparent(false);
                    bPane.setViewOrder(-1);
                }
            }

            p1.updateBorder();
            p2.updateBorder();
            p3.updateBorder();
            p4.updateBorder();

            if (p1.getSel()) {
                p2.disable();
            } else if (p2.getSel()) {
                p1.disable();
            }
            if (p3.getSel()) {
                p4.disable();
            } else if (p4.getSel()) {
                p3.disable();
            }
            if (p1.getScore() == 5) {
                p1.disable();
            }
            if (p2.getScore() == 5) {
                p2.disable();
            }
            if (p3.getScore() == 5) {
                p3.disable();
            }
            if (p4.getScore() == 5) {
                p4.disable();
            }

            if ((p1.getSel() || p2.getSel()) && (p3.getSel() || p4.getSel())) {
                if (turn) {
                    if (p1.getSel()) {
                        if (p3.getSel()) {
                            p1.add(p3.getScore());
                        } else {
                            p1.add(p4.getScore());
                        }
                    } else {
                        if (p3.getSel()) {
                            p2.add(p3.getScore());
                        } else {
                            p2.add(p4.getScore());
                        }
                    }
                } else {
                    if (p3.getSel()) {
                        if (p1.getSel()) {
                            p3.add(p1.getScore());
                        } else {
                            p3.add(p2.getScore());
                        }
                    } else {
                        if (p1.getSel()) {
                            p4.add(p1.getScore());
                        } else {
                            p4.add(p2.getScore());
                        }
                    }
                }
                p1.clearSel();
                p2.clearSel();
                p3.clearSel();
                p4.clearSel();
                turn = !turn;
            }
        });

        timeline.getKeyFrames().add(keyframe);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}