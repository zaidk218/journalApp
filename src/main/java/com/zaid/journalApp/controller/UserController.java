package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.UserCustomRepository;
import com.zaid.journalApp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserCustomRepository userCustomRepository;

    //putmapping authenticated  user ke liye rhna chahiye..
    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        log.info("Request to update user: {}", currentUsername);

        try {
            User updatedUser = userService.updateUser(currentUsername, user);
            if (updatedUser != null) {
                log.info("Successfully updated user: {}", currentUsername);
                return ResponseEntity.ok(updatedUser);
            } else {
                log.warn("User not found: {}", currentUsername);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (IllegalArgumentException e) {
            log.error("Error updating user: {}", currentUsername, e);
            return ResponseEntity.badRequest().build();
        }
    }
    //deletemapping authenticated  user ke liye rhna chahiye..

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("Request to delete user: {}", username);

        boolean isDeleted = userService.deleteUser(username);
        if (isDeleted) {
            log.info("Successfully deleted user: {}", username);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Failed to delete user or user not found: {}", username);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.info("Request to fetch user details for: {}", username);

        try {
            User user = userService.getUser(username);
            if (user != null) {
                log.info("Successfully retrieved user details for: {}", username);
                return ResponseEntity.ok(user);
            } else {
                log.warn("User not found: {}", username);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error fetching user details for: {}", username, e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
