package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUserNameOrEmail(String userName,String email);
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

}
