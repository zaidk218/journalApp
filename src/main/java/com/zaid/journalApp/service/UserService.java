package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // import @Transactional

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    // Add @Transactional to ensure the user save is done in a single transaction
    @Transactional
    public User saveEntry(User user) {
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true) // Mark as read-only for optimization when only fetching data
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true) // Mark as read-only for optimization
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    // Add @Transactional for update, ensures that the update is done as a transaction
    @Transactional
    public User updateUser(String username, User user) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    return userRepository.save(existingUser);
                })
                .orElse(null);
    }

    // Add @Transactional to ensure delete operation happens in a transaction
    @Transactional
    public boolean deleteUser(String username) {
        if (userRepository.existsByUsername(username)) {
            userRepository.deleteByUsername(username);
            return true;
        }
        return false;
    }
}
