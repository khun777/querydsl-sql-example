package com.querydsl.example.sql.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.example.sql.model.User;

public class UserRepositoryTest {
    @Inject
    private UserRepository repository;

    @Before
    public void before() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = DriverManager.getConnection("jdbc:h2:h2", "sa", "")) {
            List<String> tables = new ArrayList<String>();
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, null, new String[] {"TABLE"});
            try {
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME"));
                }
            } finally {
                rs.close();
            }

            java.sql.Statement stmt = connection.createStatement();
            try {
                stmt.execute("SET REFERENTIAL_INTEGRITY FALSE");
                for (String table : tables) {
                    stmt.execute("TRUNCATE TABLE " + table);
                }
                stmt.execute("SET REFERENTIAL_INTEGRITY TRUE");
            } finally {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void save_and_get_by_id() {
        String username = "jackie";
        User user = new User();
        user.setUsername(username);
        Long id = repository.save(user);
        assertEquals(username, repository.findById(id).getUsername());
    }

    @Test
    public void get_all() {
        User user = new User();
        user.setUsername("jimmy");
        repository.save(user);
        assertTrue(repository.all().size() == 1);
    }
}
