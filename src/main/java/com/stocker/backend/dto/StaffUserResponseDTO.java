package com.stocker.backend.dto;

import com.stocker.backend.entities.UserRole;

public record StaffUserResponseDTO(Long id, String name, String email, UserRole role) {

}
