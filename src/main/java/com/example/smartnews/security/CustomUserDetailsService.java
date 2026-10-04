package com.example.smartnews.security;

import com.example.smartnews.entity.SystemAccount;
import com.example.smartnews.repository.SystemAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private SystemAccountRepository repo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SystemAccount acc = repo.findByEmail(email).orElseThrow(
                ()->new UsernameNotFoundException("User not found"));
        return new User(acc.getEmail(), acc.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_"+acc.getRole())
                ));
    }
}
