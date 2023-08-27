
package com.tasi.user;

import com.tasi.database.DatabaseImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe de dados de Usuário.
 */
public class User extends DatabaseImpl {

    /** Sistema de LOG. */
    private static final Logger LOG = LogManager.getLogger(User.class);

    /** Máscara da senha. */
    private static final String PASSWORD_MASK = "@SEGURANCA-%s-PUCPR-2023@";
    /** Query de seleção de usuário utilizando a senha criptografada. */
    private static final String QUERY_SELECT_USER = "SELECT * FROM users WHERE user_name = '%s' AND user_password = '%s';";
    /** Query de seleção de usuário pelo nome. */
    private static final String QUERY_SELECT_USER_EXISTS = "SELECT * FROM users WHERE user_name = '%s';";

    /** Campo de nome do usuário. */
    private String name;
    /** Campo de senha do usuário. */
    private String password;

    /**
     * Classe de dados de Usuário.
     */
    public User() {
        // Construtor padrão
    }

    /**
     * Classe de dados de Usuário.
     * @param name Nome do usuário.
     * @param password Senha do usuário.
     */
    public User(String name, String password) {
        this.name = name;
        this.password = User.genPasswordHash(String.format(PASSWORD_MASK, password));
    }

    /**
     * Verifica se o usuário pode realizar login pelos dados inseridos.
     * @return indicador para usuário poder realizar login pelos dados inseridos.
     */
    public boolean checkLogin() {
        // Formata a query com o nome de usuário e senha
        String result = String.format(QUERY_SELECT_USER, this.name, this.password);
        ResultSet resultSet = this.executeQuery(result);
        String userName = getResultSetNameColumnValue(resultSet);
        return Objects.equals(userName, this.name);
    }

    /**
     * Retorna indicador de usuário existente no sistema.
     * @return indicador de usuário existente no sistema.
     */
    public boolean userExists() {
        String query = String.format(QUERY_SELECT_USER_EXISTS, this.name);
        ResultSet resultSet = this.executeQuery(query);
        return null != getResultSetNameColumnValue(resultSet);
    }

    /**
     * Retorna o valor do campo de Nome de usuário do ResultSet.
     * @param resultSet Resultado da base de dados.
     * @return o valor do campo de Nome de usuário do ResultSet.
     */
    private String getResultSetNameColumnValue(ResultSet resultSet) {
        String userName = null;
        try {
            userName = resultSet.getString("user_name");
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return userName;
    }

    private static String genPasswordHash(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
