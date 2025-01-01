package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    //createuser waala endpoint public rhega isko koi bhi access kr skt hai .
    @PostMapping("/createuser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Request to create user with username: {}", user.getUsername());

        try {
            User savedUser = userService.saveOrUpdateUser(user);
            log.info("Successfully created user with username: {}", savedUser.getUsername());
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            log.error("Error creating user with username: {}", user.getUsername(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
