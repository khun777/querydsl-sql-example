package com.querydsl.example.sql.repository;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.google.common.base.Function;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.Expression;

public abstract class AbstractRepository {
    private final ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();

    @Inject
    private DataSource dataSource;

    @Inject
    private SQLTemplates templates;

    private Connection connect() {
        return connectionHolder.get();
    }

    private SQLQuery query() {
        return new SQLQuery(connect(), templates);
    }

    protected SQLQuery from(Expression<?> expression) {
        return query().from(expression);
    }

    protected SQLInsertClause insert(RelationalPath<?> path) {
        return new SQLInsertClause(connect(), templates, path);
    }

    protected SQLUpdateClause update(RelationalPath<?> path) {
        return new SQLUpdateClause(connect(), templates, path);
    }

    protected SQLDeleteClause delete(RelationalPath<?> path) {
        return new SQLDeleteClause(connect(), templates, path);
    }

    protected <T> T tx(Function<Void, T> f) {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            connectionHolder.set(connection);
            try {
                T rv = f.apply(null);
                connection.commit();
                return rv;
            } catch (Exception e) {
                connection.rollback();
                throw e;
            } finally {
                connection.close();
                connectionHolder.remove();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}