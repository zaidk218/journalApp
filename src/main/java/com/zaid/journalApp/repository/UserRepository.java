package com.zaid.journalApp.repository;

import com.zaid.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    void deleteByUsername(String username);
}
