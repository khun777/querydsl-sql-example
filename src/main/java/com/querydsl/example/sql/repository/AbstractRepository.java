package com.querydsl.example.sql.repository;

import java.sql.Connection;

import javax.inject.Inject;

import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.Expression;
import com.querydsl.example.sql.guice.ConnectionContext;

public abstract class AbstractRepository {
    @Inject
    private SQLTemplates templates;

    @Inject
    private ConnectionContext context;

    private Connection connect() {
        return context.getConnection();
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
}
