package com.example.stocker.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stocker.dto.AuthRequest;
import com.example.stocker.entity.User;
import com.example.stocker.security.JwtService;
import com.example.stocker.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    private  JwtService jwtService;

    public AuthController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        return userService.findByEmail(email).map(user -> {
        // Comparar contraseñas
        if (user.getPassword().equals(password)) {
            String token = jwtService.generateToken(email);
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }
    }).orElseGet(() ->
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario no existe")
    );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (userService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);

        User savedUser = userService.save(newUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/clear")
    public void deleteAll() {
        userService.deleteAll();
        System.out.println("Todos los usuarios han sido eliminados");
        // Aquí podrías devolver una respuesta si lo deseas
        // return ResponseEntity.ok("Todos los usuarios han sido eliminados");
    }

}
