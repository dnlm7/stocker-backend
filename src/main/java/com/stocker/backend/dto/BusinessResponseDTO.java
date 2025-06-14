package com.stocker.backend.dto;

import com.stocker.backend.entities.Business;

import lombok.Data;

@Data
public class BusinessResponseDTO {
    private Long id;
    private String name;
    private String address;
    private String industry;
    private String description;

    public static BusinessResponseDTO fromEntity(Business business) {
        BusinessResponseDTO dto = new BusinessResponseDTO();
        dto.setId(business.getId());
        dto.setName(business.getName());
        dto.setAddress(business.getAddress());
        dto.setIndustry(business.getIndustry());
        return dto;
    }

}