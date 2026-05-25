package com.example.samplejavafxproject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

public class Player {
    private Pane pane;
    private Image img;
    private ArrayList<ImageView> chopsticks = new ArrayList<>();

    private boolean mirror;
    private boolean selected = false;
    private boolean disable = false;
    private long lastT = 0;
    private boolean flicker = false;

    private double imgFitHeight;
    private static final double FIT_WIDTH = 400;

    public Player(Pane pane, Image img, boolean mirror) {
        this.pane = pane;
        this.img = img;
        this.mirror = mirror;

        imgFitHeight = FIT_WIDTH * (img.getHeight() / img.getWidth());

        add(1);

        pane.setOnMouseClicked(event -> {
            if (disable) {
                selected = false;
            } else {
                selected = true;
            }
        });
    }

    public void add(int n) {
        if (n + getScore() >= 5) {
            n = 5 - getScore();
        }
        for (int i = 0; i < n; i++) {
            ImageView stick = new ImageView(img);
            stick.setPreserveRatio(true);
            stick.setFitWidth(FIT_WIDTH);

            Rotate rotate = new Rotate();
            rotate.setPivotX(0);
            rotate.setPivotY(imgFitHeight / 2);
            stick.getTransforms().add(rotate);
            if (mirror) {
                stick.setScaleX(-1);
            }

            chopsticks.add(stick);
            pane.getChildren().add(stick);
        }
        updateSticks();
    }

    public void clearSel() {
        pane.setStyle("-fx-border-color: none;");
        selected = false;
        flicker = false;
        disable = false;
    }

    public boolean getSel() {
        return selected;
    }

    public int getScore() {
        return chopsticks.size();
    }

    public void disable() {
        disable = true;
    }
    public void enable() {
        disable = false;
    }

    public void updateBorder() {
        if (selected) {
            long time = System.currentTimeMillis();
            if (time - lastT > 100) {
                if (flicker) {
                    pane.setStyle("-fx-border-color: none;");
                } else {
                    pane.setStyle("-fx-border-color: black;");
                }
                flicker = !flicker;
                lastT = time;
            }
        }
    }

    private void updateSticks() {
        int size = chopsticks.size();
        int dr = 5;
        int dt = 20;
        int rot = -size / 2 * dr;
        int h = -size / 2 * dt;
        double posX = 0.0;

        for (int i = 0; i < size; i++) {
            ImageView stick = chopsticks.get(i);
            Rotate rotate = (Rotate) stick.getTransforms().get(0);
            rotate.setAngle(rot);
            stick.setLayoutX(posX);
            stick.setLayoutY(h);
            rot += dr;
            h += dt;
        }
    }
}