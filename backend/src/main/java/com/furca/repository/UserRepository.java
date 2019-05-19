package com.furca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.furca.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
