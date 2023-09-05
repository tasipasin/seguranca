
package com.tasi.user;

import com.tasi.database.DatabaseImpl;
import java.sql.ResultSet;
import java.util.Objects;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Classe de dados de Usuário.
 */
public class User {

    /** Máscara da senha. */
    private static final String PASSWORD_MASK = "@SEGURANCA-%s-PUCPR-2023@";
    /** Query dos campos de seleção na tabela de usuários. */
    private static final String QUERY_SELECT_USER = "SELECT user_id, user_name, user_active FROM users";
    /** Query de seleção de usuário pelo nome. */
    private static final String QUERY_SELECT_USER_EXISTS = QUERY_SELECT_USER + " WHERE user_name = '%s'";
    /** Query de seleção de usuário utilizando a senha criptografada. */
    private static final String QUERY_SELECT_USER_PSW = QUERY_SELECT_USER_EXISTS
            + " AND user_password = '%s'";
    /** Campo de Identificador do usuário. */
    private Integer id;
    /** Campo de nome do usuário. */
    private String name;
    /** Indica usuário ativo. */
    private boolean active;

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
    public User(String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    /**
     * Retorna o identificador do usuário.
     * @return o identificador do usuário.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Determina o identificador do usuário.
     * @param id o identificador do usuário.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Retorna o Nome do usuário.
     * @return o Nome do usuário.
     */
    public String getName() {
        return name;
    }

    /**
     * Determina o Nome do usuário.
     * @param name o Nome do usuário.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Verifica se o usuário está ativo.
     * @return se o usuário está ativo.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Determina se o usuário está ativo.
     * @param active se o usuário está ativo.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Determina se o usuário está ativo.
     * @param active se o usuário está ativo.
     */
    public void setActive(Integer active) {
        this.active = Objects.equals(active, 1);
    }

    /**
     * Realiza bloqueio da conta do usuário.
     * @return bloqueio da conta do usuário.
     */
    public boolean blockAccount() {
        String query = String.format("UPDATE user SET user_active = '0' WHERE user_id = %d", this.getId());
        this.setActive(false);
        return DatabaseImpl.executeUpdate(query) > 0;
    }

    /**
     * Retorna indicador de usuário existente no sistema.
     * @return indicador de usuário existente no sistema.
     */
    public static User userExists(String name) {
        String query = String.format(QUERY_SELECT_USER_EXISTS, name);
        ResultSet resultSet = DatabaseImpl.executeQuery(query);
        return User.getResultSetToUser(resultSet);
    }

    /**
     * Verifica se o usuário pode realizar login pelos dados inseridos.
     * @return indicador para usuário poder realizar login pelos dados inseridos.
     */
    public static boolean checkLogin(String name, String password) {
        // Formata a query com o nome de usuário e senha
        String result = String.format(QUERY_SELECT_USER_PSW, name, User.genPasswordHash(password));
        ResultSet resultSet = DatabaseImpl.executeQuery(result);
        String userName = User.getResultSetNameColumnValue(resultSet);
        return Objects.equals(userName, name);
    }

    /**
     * Retorna o valor do campo de Nome de usuário do ResultSet.
     * @param resultSet Resultado da base de dados.
     * @return o valor do campo de Nome de usuário do ResultSet.
     */
    private static String getResultSetNameColumnValue(ResultSet resultSet) {
        return DatabaseImpl.getResultSetColumnValue(resultSet, "user_name");
    }

    /**
     * Retorna o objeto de usuário resultante no resultSet.
     * @param resultSet Resultado da base de dados.
     * @return o objeto de usuário resultante no resultSet.
     */
    private static User getResultSetToUser(ResultSet resultSet) {
        User result = new User();
        result.setId((Integer) DatabaseImpl.getResultSetColumnValue(resultSet, "user_id"));
        result.setName(User.getResultSetNameColumnValue(resultSet));
        result.setActive((Integer) DatabaseImpl.getResultSetColumnValue(resultSet, "user_active"));
        return result;
    }

    /**
     * Realiza tratativa da senha do usuário para retornar a hash.
     * @param password Senha do usuário.
     * @return Hash da senha do usuário.
     */
    private static String genPasswordHash(String password) {
        return DigestUtils.sha256Hex(String.format(PASSWORD_MASK, password));
    }
}
