package com.haulmont.testtask.db.dao;

import java.sql.*;
import java.util.ResourceBundle;

public abstract class AbstractDao {
    protected String jdbcDriver;
    protected String dbUrl;

    protected String user;
    protected String password;

    private Connection connection = null;

    protected AbstractDao() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("database");

        this.jdbcDriver = resourceBundle.getString("db.driver");
        this.dbUrl = resourceBundle.getString("db.url");
        this.user = resourceBundle.getString("db.username");
        this.password = resourceBundle.getString("db.password");
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(dbUrl, user, password);
    }

    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public boolean execute(final String sqlQuery) throws SQLException {
        boolean result = false;

        if (connection != null) {
            Statement statement = connection.createStatement();
            result = statement.execute(sqlQuery);

            statement.close();
        }

        return result;
    }

    public ResultSet executeQuery(final String sqlQuery) throws SQLException {
        ResultSet resultSet = null;

        if (connection != null) {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);

            statement.close();
        }

        return resultSet;
    }

    public int executeUpdate(final String sqlQuery) throws SQLException {
        int changedRowsNum = -1;

        if (connection != null) {
            Statement statement = connection.createStatement();
            changedRowsNum = statement.executeUpdate(sqlQuery);

            statement.close();
        }

        return changedRowsNum;
    }
}
