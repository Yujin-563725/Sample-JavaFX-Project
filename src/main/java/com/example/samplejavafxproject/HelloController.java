package com.example.samplejavafxproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.InputStream;

public class HelloController {
    @FXML private VBox screen;

    @FXML private Pane pane1;
    @FXML private Pane pane2;
    @FXML private Pane pane3;
    @FXML private Pane pane4;

    @FXML private Pane bPane;
    private Rectangle blocker;

    @FXML private Button b1;
    @FXML private Button b2;

    private Player p1;
    private Player p2;
    private Player p3;
    private Player p4;

    private boolean turn = false;
    private boolean turnSet = false;

    @FXML public void initialize() {
        Image img = new Image(getClass().getResourceAsStream("/chopstick.png"));

        p1 = new Player(pane1, img, false);
        p2 = new Player(pane2, img, false);
        p3 = new Player(pane3, img, true);
        p4 = new Player(pane4, img, true);

        b1.setText("Split");
        b2.setText("Split");
        b1.setOnAction(e -> {
            if (turnSet && turn) return;
            if (attemptSplit(p1, p2)) {
                turnSet = true;
                turn = true;
            }
        });
        b2.setOnAction(e -> {
            if (turnSet && !turn) return;
            if (attemptSplit(p3, p4)) {
                turnSet = true;
                turn = false;
            }
        });

        Timeline timeline = new Timeline();

        KeyFrame keyframe = new KeyFrame(Duration.millis(1000.0/60.0), event -> {
            if ((p1.getScore() == 0 && p2.getScore() == 0) || (p3.getScore() == 0 && p4.getScore() == 0)) {
                if (blocker == null) {
                    blocker = new Rectangle();
                    blocker.widthProperty().bind(bPane.widthProperty());
                    blocker.heightProperty().bind(bPane.heightProperty());
                    blocker.setFill(Color.TRANSPARENT);
                    bPane.getChildren().add(blocker);
                    bPane.setMouseTransparent(false);
                }
            }

            p1.updateBorder();
            p2.updateBorder();
            p3.updateBorder();
            p4.updateBorder();

            boolean leftSel = p1.getSel() || p2.getSel();
            boolean rightSel = p3.getSel() || p4.getSel();
            if (!turnSet) {
                if (leftSel) {
                    turn = false;
                    turnSet = true;
                } else if (rightSel) {
                    turn = true;
                    turnSet = true;
                }
            }

            p1.enable();
            p2.enable();
            p3.enable();
            p4.enable();

            if (turnSet) {
                if (!turn) {
                    if (!leftSel) {
                        p3.disable();
                        p4.disable();
                    }
                } else {
                    if (!rightSel) {
                        p1.disable();
                        p2.disable();
                    }
                }
            }

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
            if (p1.getScore() == 0) {
                p1.disable();
            }
            if (p2.getScore() == 0) {
                p2.disable();
            }
            if (p3.getScore() == 0) {
                p3.disable();
            }
            if (p4.getScore() == 0) {
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

    private boolean attemptSplit(Player a, Player b) {
        int total = a.getScore() + b.getScore();
        if (total == 0) return false;

        if (total % 2 == 0) {
            int half = total / 2;
            if (half > 4) return false;
            if (half == a.getScore() && half == b.getScore()) return false;
            applySplit(a, b, half, half);
            return true;
        } else {
            return showSplitDialog(a, b, total);
        }
    }

    private boolean showSplitDialog(Player a, Player b, int total) {
        int maxTop = Math.min(4, total);
        int minTop = Math.max(0, total - 4);
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Split");
        dialog.setHeaderText("Total chopsticks: " + total);
        dialog.setContentText("Top hand (" + minTop + "-" + maxTop + "), bottom gets the rest:");

        var result = dialog.showAndWait();
        if (result.isEmpty()) return false;
        try {
            int top = Integer.parseInt(result.get().trim());
            int bottom = total - top;
            if (top < 0 || top > 4 || bottom < 0 || bottom > 4) return false;
            if (top == a.getScore() && bottom == b.getScore()) return false;
            applySplit(a, b, top, bottom);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    private void applySplit(Player a, Player b, int topVal, int bottomVal) {
        a.set(topVal);
        b.set(bottomVal);
        p1.clearSel();
        p2.clearSel();
        p3.clearSel();
        p4.clearSel();
    }
}