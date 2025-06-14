package com.stocker.backend.dto;

import com.stocker.backend.entities.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionResponseDTO {
    private Long id;
    private String email;
    private String name;
    private BusinessDTO business;
    private UserRole role;
}
