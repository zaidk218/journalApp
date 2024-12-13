package com.zaid.journalApp.controller;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // getalluser controller hm nhi bnaayenge ,usko hm admin me bna denge ki agar admin dekhna chahta hai saare user.agar admin dekhna chahta hai saare user toh wo dekh skta hai koi ek particular user saare user nhi dekhega...
//    @GetMapping
//    public ResponseEntity<List<User>> getAllUsers(){
//        List<User> users=userService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }



    //putmapping authenticated  user ke liye rhna chahiye..

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        try {
            User updatedUser = userService.updateUser(currentUsername, user);
            return updatedUser != null
                    ? ResponseEntity.ok(updatedUser)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    //deletemapping authenticated  user ke liye rhna chahiye..

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean isDeleted = userService.deleteUser(username);
        return isDeleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }


    @GetMapping
    public ResponseEntity<User> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.getUser(username);
        return user != null
                ? ResponseEntity.ok(user)
                : ResponseEntity.notFound().build();
    }
}
