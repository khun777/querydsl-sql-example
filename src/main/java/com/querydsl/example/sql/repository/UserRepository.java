package com.querydsl.example.sql.repository;

import static com.querydsl.example.sql.model.QUser.user;

import java.util.List;

import com.querydsl.example.sql.guice.Transactional;
import com.querydsl.example.sql.model.User;

public class UserRepository extends AbstractRepository {
    @Transactional
    public User findById(final Long id) {
        return from(user).where(user.id.eq(id)).singleResult(user);
    }

    @Transactional
    public Long save(final User entity) {
        if (entity.getId() != null) {
            update(user).populate(entity).execute();
            return entity.getId();
        }
        return insert(user)
                .set(user.username, entity.getUsername())
                .executeWithKey(user.id);
    }

    @Transactional
    public List<User> all() {
        return from(user).list(user);
    }
}
