package com.example.stocker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stocker.entity.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {

}
