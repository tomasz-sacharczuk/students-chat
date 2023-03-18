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

        Group groupA = new Group("A",14);
        Group groupB = new Group("B",12);
        Group groupC = new Group("C",15);
        Group groupD = new Group("D",13);

        groupRepository.save(groupA);
        groupRepository.save(groupB);
        groupRepository.save(groupC);
        groupRepository.save(groupD);

        User admin = new User("admin",passwordEncoder.encode("admin"),
                null,"Klaudiusz","Czerwiński");
        admin.getRoles().add(adminRole);
        userRepository.save(admin);

        addUser(groupA,"Korneliusz Marciniak",userRole);
        addUser(groupA,"Bogumił Czerwiński",userRole);
        addUser(groupA,"Alan Lis",userRole);
        addUser(groupA,"Ignacy Kubiak",userRole);
        addUser(groupA,"Radosław Witkowski",userRole);
        addUser(groupA,"Milan Piotrowski",userRole);
        addUser(groupA,"Miron Kozłowski",userRole);

        addUser(groupB,"Łukasz Zakrzewska",userRole);
        addUser(groupB,"Mikołaj Kowalczyk",userRole);
        addUser(groupB,"Błażej Brzeziński",userRole);
        addUser(groupB,"Fabian Jasiński",userRole);
        addUser(groupB,"Korneliusz Zakrzewska",userRole);
        addUser(groupB,"Marcin Kwiatkowski",userRole);
        addUser(groupB,"Ryszard Włodarczyk",userRole);
        addUser(groupB,"Mieszko Andrzejewski",userRole);
        addUser(groupB,"Bruno Włodarczyk",userRole);
        addUser(groupB,"Kajetan Lis",userRole);

        addUser(groupC,"Aureliusz Kołodziej",userRole);
        addUser(groupC,"Juliusz Jakubowski",userRole);
        addUser(groupC,"Amadeusz Baran",userRole);
        addUser(groupC,"Marcin Rutkowski",userRole);
        addUser(groupC,"Joachim Laskowska",userRole);
        addUser(groupC,"Anastazy Kaczmarczyk",userRole);
        addUser(groupC,"Cezary Chmielewski",userRole);
        addUser(groupC,"Natan Mazur",userRole);
        addUser(groupC,"Fryderyk Wiśniewski",userRole);

        addUser(groupD,"Karol Głowacka",userRole);
        addUser(groupD,"Adam Jasiński",userRole);
        addUser(groupD,"Ryszard Zakrzewska",userRole);
        addUser(groupD,"Robert Nowak",userRole);
        addUser(groupD,"Leonardo Wojciechowski",userRole);
        addUser(groupD,"Alojzy Krawczyk",userRole);
        addUser(groupD,"Albert Wysocki",userRole);

    }

    private void addUser(Group group, String fullname, Role role) {
        String[] fullnameSplitted = fullname.split(" ");
        String name = fullnameSplitted[0];
        String surname = fullnameSplitted[1];
        User user = new User(name.substring(0,1)+surname,passwordEncoder.encode("user"),
                group,name,surname);
        user.getRoles().add(role);
        group.getGroupUsers().add(user);
        userRepository.save(user);
        groupRepository.save(group);
    }
}