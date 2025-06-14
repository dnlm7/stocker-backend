package com.stocker.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stocker.backend.entities.Business;
import com.stocker.backend.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByBusiness(Business business);
    boolean existsByEmail(String email);

}
