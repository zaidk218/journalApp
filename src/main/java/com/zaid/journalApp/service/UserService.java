package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
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
    public User saveOrUpdateUser(User user) {
        // Encode the password only for new users or if explicitly being updated
        if (user.getId() == null || shouldEncodePassword(user)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    private boolean shouldEncodePassword(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        return existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword());
    }


    @Transactional
    public User addJournalToUser(User user) {


        // Save only the updated user object (no password modification here)
        return userRepository.save(user);
    }


    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    // Add @Transactional for update, ensures that the update is done as a transaction
    @Transactional
    public User updateUser(String username, User user) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .map(existingUser -> {
                    // Only update fields that should be modifiable
                    if (user.getUsername() != null) {
                        existingUser.setUsername(user.getUsername());
                    }

                    // Only update password if a new password is provided
                    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    }

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
