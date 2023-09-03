
package com.tasi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementação de Interação com a base de dados.
 */
public class DatabaseImpl {

    /** Objeto de LOG. */
    private static final Logger LOG = LogManager.getLogger(DatabaseImpl.class);
    /** URL para o banco de dados. */
    private static final String DB_URL = ":resource:db/users.db";

    /**
     * Implementação de Interação com a base de dados.
     */
    private DatabaseImpl() {
        // Utility classes should not have constructors.
    }

    /**
     * Abre conexão com a base de dados.
     * @return Conexão da base de dados.
     */
    public static Connection connect() {
        Connection conn = null;
        // Monta a URL da base de dados
        String url = "jdbc:sqlite:" + DB_URL;
        try {
            // Cria a conexão com a base de dados
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return conn;
    }

    /**
     * Retorna objeto usado para realizar query na base de dados.
     * @return objeto usado para realizar query na base de dados.
     */
    private static Statement getStatement() {
        Statement stmt = null;
        Connection conn = DatabaseImpl.connect();
        if (null != conn) {
            try {
                stmt = conn.createStatement();
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
        }
        return stmt;
    }

    /**
     * Executa a query de busca informada.
     * @param query Query de busca.
     * @return O resultado da busca.
     */
    public static ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            Statement stmt = getStatement();
            if (null != stmt) {
                rs = stmt.executeQuery(query);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return rs;
    }

    /**
     * Executa a query de alteração na base de dados.
     * @param query Query de alteração de dados.
     * @return A quantidade de linhas afetadas pelo Update.
     */
    public static int executeUpdate(String query) {
        int result = -1;
        try {
            Statement stmt = getStatement();
            if (null != stmt) {
                result = stmt.executeUpdate(query);
                LOG.info(String.format("%d rows has been affected by query [%s]", result, query));
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return result;
    }

    /**
     * Retorna o valor do campo do ResultSet.
     * @param resultSet Resultado da base de dados.
     * @return o valor do campo do ResultSet.
     */
    public static <T> T getResultSetColumnValue(ResultSet resultSet, String field) {
        T result = null;
        try {
            result = (T) resultSet.getObject(field);
        } catch (SQLException ex) {
            LOG.error(ex);
        }
        return result;
    }
}
