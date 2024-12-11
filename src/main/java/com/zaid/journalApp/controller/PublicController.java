package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    //createuser waala endpoint public rhega isko koi bhi access kr skt hai .
    @PostMapping("/createuser")
    public ResponseEntity<User> createUser(@RequestBody User user) {

        User savedUser = userService.saveEntry(user);
        return ResponseEntity.ok(savedUser);
    }

}
