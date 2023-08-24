package com.tasi;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LoginController implements Initializable {
    
    @FXML
    private Button loginBtn;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private void btnClickAction(ActionEvent event) {
        errorLabel.setText("tiririca");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
