package com.furca.service;

import com.furca.model.Role;
import com.furca.model.User;
import com.furca.repository.RoleRepository;
import com.furca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
    	Set<Role> roles = roleRepository.findByRoleNames(Set.of("ROLE_EDITOR", "ROLE_ADMIN"));
    	
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        user.setUsername(user.getUsername());
        user.setName(user.getName());
        user.setEmail(user.getEmail());
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

