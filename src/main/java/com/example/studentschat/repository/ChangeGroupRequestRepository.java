package com.example.studentschat.repository;

import com.example.studentschat.entity.ChangeGroupRequest;
import com.example.studentschat.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChangeGroupRequestRepository extends CrudRepository<ChangeGroupRequest, Long> {
    Optional<ChangeGroupRequest> findChangeGroupRequestByRequestedByUser(User user);
}
