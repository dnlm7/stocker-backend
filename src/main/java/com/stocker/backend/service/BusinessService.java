package com.stocker.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocker.backend.dto.BusinessPatchDTO;
import com.stocker.backend.entities.Business;
import com.stocker.backend.entities.User;
import com.stocker.backend.repositories.BusinessRepository;
import com.stocker.backend.repositories.UserRepository;

@Service
public class BusinessService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    public Optional<Business> updateByUserEmail(String email, BusinessPatchDTO dto) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        Business business = userOpt.get().getBusiness();

        if (business == null) {
            return Optional.empty();
        }

        if (dto.getName() != null && !dto.getName().isBlank()) {
            business.setName(dto.getName());
        }

        if (dto.getIndustry() != null && !dto.getIndustry().isBlank()) {
            business.setIndustry(dto.getIndustry());
        }

        if (dto.getAddress() != null && !dto.getAddress().isBlank()) {
            business.setAddress(dto.getAddress());
        }

        return Optional.of(businessRepository.save(business));
    }

}
