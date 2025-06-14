package com.stocker.backend.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocker.backend.dto.UserPatchDTO;
import com.stocker.backend.entities.User;
import com.stocker.backend.security.TokenService;
import com.stocker.backend.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @PatchMapping("/me")
    public ResponseEntity<?> patchAuthenticatedUser(@RequestBody UserPatchDTO dto, Authentication authentication) {
        String email = authentication.getName();

        try {
            Optional<User> updatedUserOpt = userService.updatePartialByEmail(email, dto);
            if (updatedUserOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User updatedUser = updatedUserOpt.get();
            String newToken = tokenService.generateToken(updatedUser.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("user", updatedUser);
            response.put("token", newToken);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("NO papito");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            userService.delete(user.getId());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
