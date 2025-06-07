package com.example.stocker.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stocker.dto.BusinessPatchDTO;
import com.example.stocker.entity.Business;
import com.example.stocker.repository.BusinessRepository;

@Service
public class BusinessService {

    @Autowired
    private BusinessRepository businessRepository;

    public Optional<Business> updatePartial(Long id, BusinessPatchDTO dto) {
        Optional<Business> optionalBusiness = businessRepository.findById(id);

        if (optionalBusiness.isEmpty()) {
            return Optional.empty();
        }

        Business business = optionalBusiness.get();

        if (dto.getName() != null && !dto.getName().isBlank()) {
            String newName = dto.getName();
            business.setName(newName);
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
