package com.prodapt.practice.repository;

 
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.prodapt.practice.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{
    public Optional<User> findByName(String name);
}