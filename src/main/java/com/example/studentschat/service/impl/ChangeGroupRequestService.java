package com.example.studentschat.service.impl;

import com.example.studentschat.entity.ChangeGroupRequest;
import com.example.studentschat.entity.Group;
import com.example.studentschat.entity.user.User;
import com.example.studentschat.repository.ChangeGroupRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ChangeGroupRequestService {
    private ChangeGroupRequestRepository changeGroupRequestRepository;

    @Autowired
    public ChangeGroupRequestService(ChangeGroupRequestRepository changeGroupRequestRepository){
        this.changeGroupRequestRepository = changeGroupRequestRepository;
    };

    public List<ChangeGroupRequest> getAllRequestsByStatus(Integer status){
        return (List<ChangeGroupRequest>) changeGroupRequestRepository.findChangeGroupRequestsByStatus(status);
    }

    public void saveRequest(ChangeGroupRequest request) {
        changeGroupRequestRepository.save(request);
    }

    public ChangeGroupRequest getRequestById(Long id) {
        Optional<ChangeGroupRequest> requestOptional = changeGroupRequestRepository.findById(id);
        if (requestOptional.isPresent()){
            return requestOptional.get();
        }
        throw new EntityNotFoundException("No request found with given id: "+id);
    }

    public void save(ChangeGroupRequest changeGroupRequest) {
        changeGroupRequestRepository.save(changeGroupRequest);
    }

    public void delete(ChangeGroupRequest changeGroupRequests) {
        changeGroupRequestRepository.delete(changeGroupRequests);
    }

    public ChangeGroupRequest changeGroups(ChangeGroupRequest request){
        User user1 = request.getRequestedByUser();
        User user2 = request.getRequestedToUser();

        Group group1 = user1.getGroup();
        Group group2 = user2.getGroup();
        user1.setGroup(group2);
        user2.setGroup(group1);
        return request;
    }
}
