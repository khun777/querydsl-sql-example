package com.querydsl.example.sql.guice;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

public class ConnectionContext {
    @Inject
    private DataSource dataSource;

    private final ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();

    public Connection getConnection() {
        Connection connection = connectionHolder.get();
        if (connection != null) {
            return connection;
        }
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connectionHolder.set(connection);
        return connection;
    }

    public void removeConnection() {
        connectionHolder.remove();
    }
}
