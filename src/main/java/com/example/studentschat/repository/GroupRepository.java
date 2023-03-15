package com.example.studentschat.repository;

import com.example.studentschat.entity.Group;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface GroupRepository extends CrudRepository<Group, Long> {
    Optional<Group> getGroupByName(String name);
}
