package com.stocker.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocker.backend.dto.CreateUserRequestDTO;
import com.stocker.backend.dto.StaffUserResponseDTO;
import com.stocker.backend.entities.User;
import com.stocker.backend.entities.UserRole;
import com.stocker.backend.repositories.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/business/staff")
public class StaffUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequestDTO request,
            Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != UserRole.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Solo administradores pueden acceder");
        }

        if (userRepository.existsByEmail(request.getEmail().toLowerCase())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está en uso");
        }

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setBusiness(currentUser.getBusiness());
        newUser.setRole(request.getRole());

        userRepository.save(newUser);

        return ResponseEntity.ok(
                new StaffUserResponseDTO(newUser.getId(), newUser.getName(), newUser.getEmail(), newUser.getRole()));
    }

}
