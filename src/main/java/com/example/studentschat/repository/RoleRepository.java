package com.example.studentschat.repository;

import com.example.studentschat.entity.user.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role,Long> {
}
