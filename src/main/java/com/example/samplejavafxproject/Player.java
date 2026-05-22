package com.example.samplejavafxproject;

import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class Player extends Label {
    private int score = 0;

    public Player() {
        super("0");
    }

    public Player(@NamedArg("text") String text) {
        super(text);
    }

    public void add(int n) {
        score += n;
        setText(String.valueOf(score));
    }

    public int getScore() {
        return score;
    }
}
