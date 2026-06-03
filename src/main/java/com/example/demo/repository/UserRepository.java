package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users where username = :username", nativeQuery = true)
    Optional<User> findByUsername(@Param("username") String username);

    @Query(value = "select *  from users where email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    @Query(value = "select count(*) > 0 from users where username = :username", nativeQuery = true)
    int existsByUsername(@Param("username") String username);

    @Query(value = "select count(*) > 0 from users where email = :email", nativeQuery = true)
    int existsByEmail(@Param("email") String email);

}
