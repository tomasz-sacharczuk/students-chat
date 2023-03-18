package com.example.studentschat.component;

import com.example.studentschat.entity.ChangeGroupRequest;
import com.example.studentschat.entity.Group;
import com.example.studentschat.entity.user.Role;
import com.example.studentschat.entity.user.User;
import com.example.studentschat.repository.ChangeGroupRequestRepository;
import com.example.studentschat.repository.GroupRepository;
import com.example.studentschat.repository.RoleRepository;
import com.example.studentschat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
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

    @Autowired
    ChangeGroupRequestRepository changeGroupRequestRepository;

    @Autowired
    GroupRepository groupRepository;

    @PostConstruct
    @Transactional
    void createUser(){
        Role adminRole = new Role("ADMIN");
        Role userRole = new Role("USER");

        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);

        Group groupA = new Group("A",2);
        Group groupB = new Group("B",10);
        groupRepository.save(groupA);
        groupRepository.save(groupB);

        User user = new User("user",passwordEncoder.encode("user"),
                groupA,"Thomas","Werner");
        user.getRoles().add(userRole);
        groupA.getGroupUsers().add(user);

        User admin = new User("admin",passwordEncoder.encode("admin"),
                groupB,"Christopher","Barteczko");
        admin.getRoles().add(adminRole);
        groupB.getGroupUsers().add(admin);

        User user2 = new User("user2",passwordEncoder.encode("user2"),
                groupA,"Peter","Gakko");
        user2.getRoles().add(userRole);

        groupA.getGroupUsers().add(user2);

        groupRepository.save(groupA);
        groupRepository.save(groupB);

        LocalDateTime now = LocalDateTime.now();
        user.setChatBanEndTime(now.plusMinutes(2));

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(admin);

        ChangeGroupRequest changeGroupRequest = new ChangeGroupRequest(user,admin);

        changeGroupRequestRepository.save(changeGroupRequest);

        user.getChangeGroupByRequests().add(changeGroupRequest);
        admin.getChangeGroupToRequests().add(changeGroupRequest);

        userRepository.save(user);
        userRepository.save(admin);
    }
}
