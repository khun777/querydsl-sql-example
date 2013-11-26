package com.querydsl.example.sql.repository;

import static com.querydsl.example.sql.model.QUser.user;

import java.util.List;

import com.google.common.base.Function;
import com.querydsl.example.sql.model.User;

public class UserRepository extends AbstractRepository {
    public User findById(final Long id) {
        return tx(new Function<Void, User>() {
            @Override
            public User apply(Void input) {
                return from(user).where(user.id.eq(id)).singleResult(user);
            }
        });
    }

    public Long save(final User entity) {
        return tx(new Function<Void, Long>() {
            @Override
            public Long apply(Void input) {
                if (entity.getId() != null) {
                    update(user).populate(entity).execute();
                    return entity.getId();
                }
                return insert(user)
                        .set(user.username, entity.getUsername())
                        .executeWithKey(user.id);
            }
        });
    }

    public List<User> all() {
        return tx(new Function<Void, List<User>>() {
            @Override
            public List<User> apply(Void input) {
                return from(user).list(user);
            }
        });
    }
}
