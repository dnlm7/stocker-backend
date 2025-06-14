package com.stocker.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stocker.backend.entities.Business;

public interface BusinessRepository extends JpaRepository<Business, Long> {


    
} 