package com.querydsl.example.sql.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.junit.Test;

import com.querydsl.example.sql.model.User;

public class UserRepositoryTest extends AbstractPersistenceTest {
    @Inject
    private UserRepository repository;

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
