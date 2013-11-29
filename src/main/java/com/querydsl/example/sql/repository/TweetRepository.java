package com.querydsl.example.sql.repository;

import static com.querydsl.example.sql.model.QLocation.location;
import static com.querydsl.example.sql.model.QTweet.tweet;
import static com.querydsl.example.sql.model.QUser.user;

import java.util.List;

import com.mysema.query.types.Predicate;
import com.querydsl.example.sql.guice.Transactional;
import com.querydsl.example.sql.model.Tweet;

public class TweetRepository extends AbstractRepository {
    @Transactional
    public Long save(Tweet entity) {
        if (entity.getId() != null) {
            update(tweet).populate(entity).execute();
            return entity.getId();
        }
        return insert(tweet).populate(entity)
                .executeWithKey(user.id);
    }

    @Transactional
    public Tweet findById(Long id) {
        return from(tweet).where(tweet.id.eq(id)).singleResult(tweet);
    }
    
    @Transactional
    public List<Tweet> findOfUser(String username) {
        return from(user)
                .innerJoin(tweet).on(tweet.posterId.eq(user.id))
                .list(tweet);
    }
    
    @Transactional
    public List<Tweet> findOfArea(double[] pointA, double[] pointB) {
        return from(tweet)
                .innerJoin(location).on(tweet.locationId.eq(location.id))
                .where(location.longitude.between(pointA[0], pointB[0]),
                       location.latitude.between(pointA[1], pointB[1]))
                .list(tweet);                       
    }

    @Transactional
    public List<Tweet> findAll(Predicate expr) {
        return from(tweet).where(expr).list(tweet);
    }
}
