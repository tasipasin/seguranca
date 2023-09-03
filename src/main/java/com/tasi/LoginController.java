
package com.tasi;

import com.tasi.user.User;
import com.tasi.utils.ValidationUtils;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Classe de Controle da Tela de Login.
 */
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
    private static final Integer MAX_ATTEMPTS = 5;
    /** Mapeamento da quantidade de tentativas de login por usuário. */
    private final Map<Integer, Integer> attemptsMap = new HashMap<>();

    /**
     * Ação executada ao clicar no botão de login.
     * @param event Evento disparado pelo botão.
     */
    @FXML
    private void btnClickAction(ActionEvent event) {
        String message = "Campo de usuário não pode estar vazio";
        // Recupera o usuário inserido no campo
        String insertedUser = usrTextField.getText();
        if (ValidationUtils.isNotEmpty(insertedUser)) {
            User usr = User.userExists(insertedUser);
            if (null == usr.getId()) {
                // Exibe mensagem de usuário não existente
                message = "Usuário não existe";
            } else if (!usr.isActive() || this.attemptsMap.getOrDefault(usr.getId(), 0) >= MAX_ATTEMPTS) {
                // Exibe mensagem de usuário não ativo
                message = "Conta bloqueada! Muitas tentativas erradas.";
            } else {
                String insertedPsw = pswTextField.getText();
                if (User.checkLogin(usr.getName(), insertedPsw)) {
                    message = "Usuário logado.";
                } else {
                    Integer attempts = updateAttemptsMap(usr.getId());
                    message = String.format("Login ou senha incorretos. Tentativas restantes %d",
                            MAX_ATTEMPTS - attempts);
                    if (attempts > 5) {
                        message = "Conta bloqueada! Muitas tentativas erradas.";
                    }
                }
            }
        }
        errorLabel.setText(message);
    }

    /**
     * Atualiza a quantidade de tentativas do usuário.
     * @param userId Identificador do usuário.
     * @return quantidade de tentativas do usuário.
     */
    private Integer updateAttemptsMap(Integer userId) {
        Integer currAttempts = this.attemptsMap.getOrDefault(userId, 0) + 1;
        this.attemptsMap.put(userId, currAttempts);
        return currAttempts;
    }

    @FXML
    private void btnResetAction(ActionEvent event) {
        attemptsMap.clear();
        errorLabel.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // do nothing
    }
}
