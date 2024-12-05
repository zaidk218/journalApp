package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.saveEntry(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/username/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User user) {
        User updatedUser = userService.updateUser(username, user);
        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        boolean isDeleted = userService.deleteUser(username);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        User user = userService.getUser(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
}
