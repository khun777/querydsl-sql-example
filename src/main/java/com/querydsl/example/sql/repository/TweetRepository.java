package com.querydsl.example.sql.repository;

import static com.querydsl.example.sql.model.QTweet.tweet;
import static com.querydsl.example.sql.model.QUser.user;

import java.util.List;

import com.mysema.query.types.expr.BooleanExpression;
import com.querydsl.example.sql.guice.Transactional;
import com.querydsl.example.sql.model.Tweet;

public class TweetRepository extends AbstractRepository {
    @Transactional
    public Long save(Tweet entity) {
        if (entity.getId() != null) {
            update(tweet).populate(entity).execute();
            return entity.getId();
        }
        return insert(tweet)
                .set(tweet.content, entity.getContent())
                .set(tweet.locationId, entity.getLocationId())
                .set(tweet.posterId, entity.getPosterId())
                .executeWithKey(user.id);
    }

    @Transactional
    public Tweet findById(Long id) {
        return from(tweet).where(tweet.id.eq(id)).singleResult(tweet);
    }

    @Transactional
    public List<Tweet> findAll(BooleanExpression expr) {
        return from(tweet).where(expr).list(tweet);
    }
}
