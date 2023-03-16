package com.example.studentschat.service.impl;

import com.example.studentschat.entity.Group;
import com.example.studentschat.entity.user.User;
import com.example.studentschat.repository.GroupRepository;
import com.example.studentschat.repository.UserRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class GroupService {

    private GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository){
        this.groupRepository = groupRepository;
    };

    public Group getGroupById(Long groupId){
        Optional<Group> groupOptional = groupRepository.getGroupById(groupId);
        if(groupOptional.isPresent()) {
            return groupOptional.get();
        }
        throw new ObjectNotFoundException(groupId, "group");
    }

    public Iterable<Group> getAllGroups(){
        return groupRepository.findAll();
    }
}