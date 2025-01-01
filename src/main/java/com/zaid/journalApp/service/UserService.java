package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // import @Transactional

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    // Add @Transactional to ensure the user save is done in a single transaction


    @Transactional
    public User saveOrUpdateUser(User user) {
        log.info("Attempting to save/update user: {}", user.getUsername());
        try {
            if (user.getId() == null || shouldEncodePassword(user)) {
                log.debug("Encoding password for user: {}", user.getUsername());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                User existingUser = userRepository.findByUsername(user.getUsername());
                if (existingUser != null) {
                    log.debug("Merging roles for existing user: {}", user.getUsername());
                    List<String> updatedRoles = new ArrayList<>(existingUser.getRoles());
                    for (String role : user.getRoles()) {
                        if (!updatedRoles.contains(role)) {
                            updatedRoles.add(role);
                        }
                    }
                    user.setRoles(updatedRoles);
                }
            } else {
                log.debug("Assigning default USER role to: {}", user.getUsername());
                user.setRoles(List.of("USER"));
            }
            User savedUser = userRepository.save(user);
            log.info("Successfully saved/updated user: {}", user.getUsername());
            return savedUser;
        } catch (Exception e) {
            log.error("Error saving/updating user: {}", user.getUsername(), e);
            throw e;
        }
    }

    @Transactional
    public User saveOrUpdateAdminUser(User user) {
        log.info("Attempting to save/update admin user: {}", user.getUsername());
        user.getRoles().add("ADMIN");
        user.getRoles().add("USER");
        return saveOrUpdateUser(user);
    }




    // saveOrUpdateAdminUser

    private boolean shouldEncodePassword(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        return existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword());
    }


    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        log.debug("Retrieved {} users", users.size());
        return users;
    }

    @Transactional(readOnly = true)
    public User getUser(String username) {
        log.debug("Fetching user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    // Add @Transactional for update, ensures that the update is done as a transaction
    @Transactional
    public User updateUser(String username, User user) {
        log.info("Updating user: {}", username);
        return Optional.ofNullable(userRepository.findByUsername(username))
                .map(existingUser -> {
                    if (user.getUsername() != null) {
                        log.debug("Updating username from {} to {}", username, user.getUsername());
                        existingUser.setUsername(user.getUsername());
                    }

                    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                        log.debug("Updating password for user: {}", username);
                        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    }

                    User updatedUser = userRepository.save(existingUser);
                    log.info("Successfully updated user: {}", username);
                    return updatedUser;
                })
                .orElseGet(() -> {
                    log.warn("User not found for update: {}", username);
                    return null;
                });
    }

    // Add @Transactional to ensure delete operation happens in a transaction
    @Transactional
    public boolean deleteUser(String username) {
        log.info("Attempting to delete user: {}", username);
        if (userRepository.existsByUsername(username)) {
            userRepository.deleteByUsername(username);
            log.info("Successfully deleted user: {}", username);
            return true;
        }
        log.warn("User not found for deletion: {}", username);
        return false;
    }
}
