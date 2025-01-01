package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController
{
    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        log.info("Admin request to fetch all users");
        try {
            List<User> users = userService.getAllUsers();
            log.debug("Retrieved {} users", users != null ? users.size() : 0);
            return (users==null||users.isEmpty()) ?
                    ResponseEntity.noContent().build() :
                    ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching all users", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/createAdminUser")
    public ResponseEntity<User> createAdminUser(@RequestBody User user) {
        log.info("Request to create admin user: {}", user.getUsername());
        try {
            User savedUser = userService.saveOrUpdateAdminUser(user);
            log.info("Successfully created admin user: {}", savedUser.getUsername());
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            log.error("Error creating admin user: {}", user.getUsername(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
