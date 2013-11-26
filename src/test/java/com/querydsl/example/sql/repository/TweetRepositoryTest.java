package com.querydsl.example.sql.repository;

import static com.querydsl.example.sql.model.QTweet.tweet;
import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Test;

import com.querydsl.example.sql.model.Tweet;
import com.querydsl.example.sql.model.User;

public class TweetRepositoryTest extends AbstractPersistenceTest {
    @Inject
    private TweetRepository repository;

    @Inject
    private UserRepository userRepository;

    @Test
    public void save_and_find_by_id() {
        User poster = new User();
        poster.setUsername("dr_frank");
        Long posterId = userRepository.save(poster);

        String content = "I am alive! #YOLO";
        Tweet tweet = new Tweet();
        tweet.setContent(content);
        tweet.setPosterId(posterId);
        Long id = repository.save(tweet);
        assertEquals(content, repository.findById(id).getContent());
    }

    @Test
    public void find_list_by_predicate() {
        User poster = new User();
        poster.setUsername("dr_frank");
        Long posterId = userRepository.save(poster);

        Tweet tw1 = new Tweet();
        tw1.setPosterId(posterId);
        tw1.setContent("It is a alive! #YOLO");
        repository.save(tw1);

        Tweet tw2 = new Tweet();
        tw2.setPosterId(posterId);
        tw2.setContent("Oh the humanity!");
        repository.save(tw2);

        Tweet tw3 = new Tweet();
        tw3.setPosterId(posterId);
        tw3.setContent("#EpicFail");
        repository.save(tw3);

        assertEquals(1, repository.findAll(tweet.content.contains("#YOLO")).size());
    }

    @Test
    public void find_list_by_complex_predicate() {
//        List<String> usernames = Lists.newArrayList("dr_frank", "mike", "maggie", "liza");
//        List<User> users = Lists.newArrayList();
//        for (String username : usernames) {
//            users.add(userRepository.save(new User(username)));
//        }
//        User poster = new User("duplo");
//        userRepository.save(poster);
//        for (int i = 0; i < 100; ++i) {
//            repository.save(new Tweet(poster, "spamming @dr_frank " + i, users.subList(0, 1), null));
//        }
//        assertTrue(repository.findAll(tweet.mentions.contains(users.get(1))).isEmpty());
//
//        assertEquals(100, repository.findAll(tweet.mentions.contains(users.get(0))).size());
//
//        assertTrue(repository.findAll(tweet.mentions.any().username.eq("duplo")).isEmpty());
//
//        assertEquals(100, repository.findAll(tweet.mentions.any().username.eq("dr_frank")).size());
    }
}
