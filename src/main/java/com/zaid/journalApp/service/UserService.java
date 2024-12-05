package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveEntry(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean deleteUser(String username) {
        if (userRepository.existsByUsername(username)) {
            userRepository.deleteByUsername(username);
            return true;
        }
        return false;
    }

    public User updateUser(String username, User user) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (existingUser.isPresent()) {
            User entryToUpdate = existingUser.get();
            entryToUpdate.setUsername(user.getUsername());
            entryToUpdate.setPassword(user.getPassword());
            return userRepository.save(entryToUpdate);
        }
        return null;
    }
}
