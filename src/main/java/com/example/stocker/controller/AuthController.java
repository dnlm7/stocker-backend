package com.example.stocker.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stocker.dto.AuthRegisterRequest;
import com.example.stocker.dto.AuthRequest;
import com.example.stocker.dto.BusinessDTO;
import com.example.stocker.dto.UserResponseDTO;
import com.example.stocker.entity.Business;
import com.example.stocker.entity.User;
import com.example.stocker.repository.BusinessRepository;
import com.example.stocker.security.JwtService;
import com.example.stocker.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BusinessRepository businessRepository;

    private JwtService jwtService;

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
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario no existe"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRegisterRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String name = request.getName();

        if (userService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado");
        }

        // Crear el negocio vacío
        Business emptyBusiness = new Business();
        emptyBusiness.setName(""); // o null si prefieres
        Business savedBusiness = businessRepository.save(emptyBusiness);

        // Crear el usuario y asociar el negocio
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setName(name);
        newUser.setBusiness(savedBusiness);

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

    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no proporcionado o inválido");
        }

        String token = authHeader.substring(7); // Eliminar "Bearer "

        String email = jwtService.validateToken(token);
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
        }

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Business business = user.getBusiness();

            BusinessDTO businessDTO = null;
            if (business != null) {
                businessDTO = new BusinessDTO(
                        business.getId(),
                        business.getName(),
                        business.getTicker(),
                        business.getSector(),
                        business.getIndustry(),
                        business.getAddress(),
                        business.getDescription());
            }

            UserResponseDTO dto = new UserResponseDTO(user.getId(), user.getEmail(), user.getName(), businessDTO);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

}
