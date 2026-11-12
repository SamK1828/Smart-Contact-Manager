package com.smart.config;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepository;
import com.smart.models.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsPasswordService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("No account found for that email."));
        return new CustomUserDetails(user);
    }

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
        User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("No account found for that email."));
        user.setPassword(newPassword);
        return new CustomUserDetails(userRepository.save(user));
    }
}
