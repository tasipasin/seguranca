
package com.tasi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class DatabaseImpl {

    private static final Logger LOG = LogManager.getLogger(DatabaseImpl.class);
    private static final String DB_URL = ":resource:db/users.db";

    /**
     * 
     * @return
     */
    public static Connection connect() {
        Connection conn = null;
        // db parameters
        String url = "jdbc:sqlite:" + DB_URL;
        try {
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            LOG.info("Connection to SQLite has been established.");
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return conn;
    }

    /**
     * 
     * @param query
     * @return
     */
    public ResultSet executeQuery(String query) {
        ResultSet rs = null;
        Statement stmt = null;
        Connection conn = null;
        try {
            conn = DatabaseImpl.connect();
            if (null != conn) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(query);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return rs;
    }
}
