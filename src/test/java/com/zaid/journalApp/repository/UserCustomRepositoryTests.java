package com.zaid.journalApp.repository;

import com.zaid.journalApp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserCustomRepositoryTests {

    @Autowired
    private UserCustomRepository userCustomRepository;

    @Test
    public void testFindUsersWithSentimentAnalysis() {
        List<User> users = userCustomRepository.findUsersWithSentimentAnalysis(true);
        assertThat(users).isNotNull();
        users.forEach(user -> {
            assertThat(user.getEmail()).matches("^([a-zA-Z0-9_.-])+[@](([a-zA-Z0-9_.-])+[.])+([a-zA-Z0-9_.-]){2,4}$");
            assertThat(user.isSentimentAnalysis()).isTrue();
        });
    }
}
