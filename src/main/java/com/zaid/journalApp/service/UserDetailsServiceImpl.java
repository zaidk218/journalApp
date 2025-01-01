package com.zaid.journalApp.service;

import com.zaid.journalApp.entity.User;
import com.zaid.journalApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Preferred over @Component for service classes
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user details for username: {}", username);

        User user = userRepository.findByUsername(username);
        if (user != null) {
            log.info("User found: {}, Roles: {}", username, user.getRoles());

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles() != null ? user.getRoles().toArray(new String[0]) : new String[0])
                    .build();
        }

        log.error("User not found: {}", username);
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
