package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.saveEntry(user);
    }

    @PutMapping("/username/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User user){
        if (user == null || user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            return null;
        }
        return userService.updateUser(username, user);
    }

    @DeleteMapping("/username/{username}")
    public boolean deleteUser(@PathVariable String username){
        return userService.deleteUser(username);
    }

    @GetMapping("/username/{username}")
    public User getUser(@PathVariable String username){
        return userService.getUser(username);
    }
}
