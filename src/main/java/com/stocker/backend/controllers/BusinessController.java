package com.stocker.backend.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocker.backend.dto.BusinessPatchDTO;
import com.stocker.backend.dto.BusinessResponseDTO;
import com.stocker.backend.dto.UserDTO;
import com.stocker.backend.entities.Business;
import com.stocker.backend.entities.User;
import com.stocker.backend.entities.UserRole;
import com.stocker.backend.repositories.UserRepository;
import com.stocker.backend.service.BusinessService;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessService businessService;

    @PatchMapping("/me")
    public ResponseEntity<?> patchAuthenticatedBusiness(@RequestBody BusinessPatchDTO dto,
            Authentication authentication) {
        String email = authentication.getName();

        try {
            Optional<Business> updatedBusinessOpt = businessService.updateByUserEmail(email, dto);

            if (updatedBusinessOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Business updatedBusiness = updatedBusinessOpt.get();
            BusinessResponseDTO responseDTO = BusinessResponseDTO.fromEntity(updatedBusiness);

            return ResponseEntity.ok(responseDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsersFromBusiness(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
        }

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo administradores pueden acceder");
        }

        Business business = currentUser.getBusiness();

        if (business == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se encontró la empresa asociada al usuario");
        }

        List<User> users = userRepository.findByBusiness(business);
        List<UserDTO> dtos = users.stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}
