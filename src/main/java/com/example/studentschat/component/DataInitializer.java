package com.example.studentschat.component;

import com.example.studentschat.entity.user.Role;
import com.example.studentschat.entity.user.User;
import com.example.studentschat.repository.RoleRepository;
import com.example.studentschat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @PostConstruct
    void createUser(){

        Role adminRole = new Role("ADMIN");
        Role userRole = new Role("USER");

        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);

        userRepository.save(new User("user",passwordEncoder.encode("user"),
                userRoles));
        userRepository.save(new User("admin",passwordEncoder.encode("admin"),
                adminRoles));
    }
}
