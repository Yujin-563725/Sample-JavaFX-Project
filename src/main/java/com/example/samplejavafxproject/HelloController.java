package com.example.samplejavafxproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HelloController {

    @FXML
    private Button b1;
    @FXML
    private Button b2;

    @FXML
    private Player score1;
    @FXML
    private Player score2;

    @FXML
    public void initialize() {
    }

    @FXML
    protected void b1Click() {
        score1.add(1);
    }

    @FXML
    protected void b2Click() {
        score2.add(1);
    }
}