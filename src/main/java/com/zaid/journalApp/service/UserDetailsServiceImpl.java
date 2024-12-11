package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Preferred over @Component for service classes
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user by username from the repository
        User user = userRepository.findByUsername(username);

        // If the user is found, build and return UserDetails
        if (user != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles() != null ? user.getRoles().toArray(new String[0]) : new String[0])
                    .build();
        }

        // Throw exception if user is not found
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
