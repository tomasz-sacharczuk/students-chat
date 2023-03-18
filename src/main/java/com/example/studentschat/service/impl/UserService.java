package com.example.studentschat.service.impl;

import com.example.studentschat.entity.user.User;
import com.example.studentschat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
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
        return findUserByUsername(userName);
    }

    public String getUserCredentialsWithFirstLetterOfSurname(User user){
        Assert.notNull(user.getName(),"Given user has null name field!");
        Assert.notNull(user.getSurname(),"Given user has null surname field!");

        String finalCredentials = user.getName() + " " + user.getSurname().charAt(0) + ".";
        return finalCredentials;
    }

    public Optional<User> findOptionalUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User findUserByUsername(String userName){
        Optional<User> userOptional = userRepository.findByUsername(userName);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new UsernameNotFoundException("No user found with username: " + userName);
    }

    public boolean isUserBanned(User user){
        if (user.getChatBanEndTime() == null ||
                (user.getChatBanEndTime() != null && user.getChatBanEndTime().isBefore(LocalDateTime.now()))){
            return false;
        }
        return true;
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }
}
