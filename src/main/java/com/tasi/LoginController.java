
package com.tasi;

import com.tasi.user.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

    /** Campo de nome de Usuário. */
    @FXML
    private TextField usrTextField;
    /** Campo da senha do usuário. */
    @FXML
    private PasswordField pswTextField;
    /** Botão de login. */
    @FXML
    private Button loginBtn;
    /** Label de mensagem de erro. */
    @FXML
    private Button resetBtn;
    /** Label de mensagem de erro. */
    @FXML
    private Label errorLabel;

    /** Controles de falha de login. */
    private Integer cont = 1;
    private boolean accountBlocked = false;
    private static final Integer MAX_ATTEMPTS = 5;

    @FXML
    private void btnClickAction(ActionEvent event) {
        // TODO: realizar verificação por usuário
        String message = "Campo de usuário não pode estar vazio";
        if (null != usrTextField.getText() && !usrTextField.getText().trim().isEmpty()) {
            User user = new User(usrTextField.getText(), pswTextField.getText());
            if (!accountBlocked) {
                if (user.checkLogin()) {
                    message = "Usuário logado.";
                } else {
                    message = String.format("Login ou senha incorretos. Tentativas restantes %d", MAX_ATTEMPTS - cont);
                    cont += 1;
                    if (cont > 5) {
                        accountBlocked = true;
                        message = "Conta bloqueada! Muitas tentativas erradas.";
                    }
                }
            }
        }
        errorLabel.setText(message);
    }

    @FXML
    private void btnResetAction(ActionEvent event) {
        cont = 1;
        accountBlocked = false;
        errorLabel.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
}
