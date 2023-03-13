package com.example.studentschat.repository;

import java.util.Optional;

import com.example.studentschat.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
	
	Optional<User> findByUsername(String username);

}
