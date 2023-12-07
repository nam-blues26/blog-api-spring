package com.blog.repositories;

import com.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    Optional<User> findById(long id);
    //SELECT * FROM users WHERE phoneNumber=?
}
