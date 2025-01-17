package com.zaid.journalApp.repository;

import com.zaid.journalApp.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class UserCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserCustomRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<User> findUsersWithSentimentAnalysis(boolean sentimentEnabled) {
        try {

            Criteria emailCriteria = Criteria.where("email")
                    .regex("^([a-zA-Z0-9_.-])+[@](([a-zA-Z0-9_.-])+[.])+([a-zA-Z0-9_.-]){2,4}$");

            // Creating criteria for condition b (sentiment analysis)
            Criteria sentimentCriteria = Criteria.where("sentimentAnalysis")
                    .is(sentimentEnabled);

            // (a && b)
            Criteria andPart = new Criteria().andOperator(
                    emailCriteria,
                    sentimentCriteria
            );

            // (a || b)
            Criteria orPart = new Criteria().orOperator(
                    emailCriteria,
                    sentimentCriteria
            );

            // Final criteria: (a && b) || (a || b)
            Criteria finalCriteria = new Criteria().orOperator(
                    andPart,
                    orPart
            );

            Query query = new Query(finalCriteria);

            return mongoTemplate.find(query, User.class);


        }catch (Exception e) {
            log.error("Error fetching users with sentiment analysis enabled:{} : {}", sentimentEnabled, e.getMessage(),e);
            throw  new RuntimeException("Error fetching users with sentiment analysis enabled",e);
        }

    }
}
