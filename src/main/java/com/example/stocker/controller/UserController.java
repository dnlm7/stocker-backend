package com.example.stocker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stocker.dto.UserPatchDTO;
import com.example.stocker.entity.User;
import com.example.stocker.security.JwtService;
import com.example.stocker.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public List<User> getAll() {
        System.out.println("Entró a getAll()");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Agregar un nuevo usuario
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {

        if (user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Email y contraseña son obligatorios");
        }

        // Verifica si el email ya existe
        // if (userService.existsByEmail(user.getEmail())) {
        // return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está
        // registrado.");
        // }

        User createdUser = userService.save(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUser(@PathVariable Long id, @RequestBody UserPatchDTO dto) {
        try {
            Optional<User> updatedUserOpt = userService.updatePartial(id, dto);
            if (updatedUserOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            User updatedUser = updatedUserOpt.get();
            String newToken = jwtService.generateToken(updatedUser.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("user", updatedUser);
            response.put("token", newToken);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

}
