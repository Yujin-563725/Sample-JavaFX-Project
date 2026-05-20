package com.example.samplejavafxproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HelloController {
    static class Stick {
        @FXML
        private final Label label;
        private int score = 0;

        Stick(Label label) {
            this.label = label;//
        }

        void add(int n) {
            score += n;
            label.setText(Integer.toString(score));
        }
    }

    @FXML
    private Label score1;
    @FXML
    private Label score2;
    @FXML
    private Button b1;
    @FXML
    private Button b2;

    private Stick st1 = new Stick(score1);
    private Stick st2 = new Stick(score2);

    @FXML
    protected void b1Click() {
        st1.add(1);
    }

    @FXML
    protected void b2Click() {
        st2.add(1);
    }
}