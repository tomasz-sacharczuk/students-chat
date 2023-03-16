package com.example.studentschat.service.impl;

import com.example.studentschat.entity.user.User;
import com.example.studentschat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

@Service
public class UserService {

    private static final String USERNAME_CANNOT_BE_NULL = "Username is null!";

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    };

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = null;
        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }

        Assert.notNull(userName, USERNAME_CANNOT_BE_NULL);

        Optional<User> currentUser = userRepository.findByUsername(userName);
        if (currentUser.isPresent()) {
            return currentUser.get();
        }
        throw new UsernameNotFoundException("No user found with username: " + userName);
    }

    public String getUserCredentialsWithFirstLetterOfSurname(User user){
        Assert.notNull(user.getName(),"Given user has null name field!");
        Assert.notNull(user.getSurname(),"Given user has null surname field!");

        String finalCredentials = user.getName() + " " + user.getSurname().charAt(0) + ".";
        return finalCredentials;
    }
}
