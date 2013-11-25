package com.querydsl.example.sql.repository;

import static com.querydsl.example.sql.model.QUser.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.mysema.query.sql.H2Templates;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.EntityPath;
import com.querydsl.example.sql.model.User;

public class UserRepository {
    private final DataSource dataSource;

    @Inject
    public UserRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection connect() throws SQLException {
            return dataSource.getConnection();
    }

    private static final SQLTemplates DIALECT = new H2Templates();

    private SQLQuery from(EntityPath<?> path) throws SQLException {
        SQLQuery query = new SQLQuery(connect(), DIALECT);
        return query.from(path);
    }

    public User findById(Long id) {
        try (Connection connection = connect()) {
            return from(user).where(user.id.eq(id)).singleResult(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long save(User entity) {
        try (Connection connection = connect()) {
            if (entity.getId() != null) {
                new SQLUpdateClause(connection, DIALECT, user)
                        .populate(entity)
                        .execute();
                return entity.getId();
            }
            return new SQLInsertClause(connection, DIALECT, user)
                    .set(user.username, entity.getUsername())
                    .executeWithKey(user.id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> all() {
        try (Connection connection = connect()) {
            return from(user).list(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
