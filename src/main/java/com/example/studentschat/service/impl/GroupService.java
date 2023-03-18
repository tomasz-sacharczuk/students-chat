package com.example.studentschat.service.impl;

import com.example.studentschat.entity.Group;
import com.example.studentschat.repository.GroupRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

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
        throw new EntityNotFoundException("No group found with given id: "+groupId);
    }

    public List<Group> getAllGroups(){
        return (List<Group>) groupRepository.findAll();
    }
}