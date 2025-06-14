package com.stocker.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.stocker.backend.dto.UserPatchDTO;
import com.stocker.backend.entities.User;
import com.stocker.backend.repositories.UserRepository;

// import com.example.stocker.dto.CreateUserRequest;
// import com.example.stocker.dto.UserPatchDTO;
// import com.example.stocker.entity.User;
// import com.example.stocker.entity.enums.Role;
// import com.example.stocker.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public Optional<User> updatePartialByEmail(String email, UserPatchDTO dto) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();

        // Actualizar nombre
        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }

        // Actualizar email
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            String newEmail = dto.getEmail();

            // Verificar si el nuevo email ya está siendo usado por otro usuario
            Optional<User> existingByEmail = userRepository.findByEmail(newEmail);
            if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(user.getId())) {
                throw new IllegalArgumentException("El email ya está en uso por otro usuario");
            }

            user.setEmail(newEmail);
        }

        // Actualizar contraseña (y encriptar)
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            String hashedPassword = passwordEncoder.encode(dto.getPassword());
            user.setPassword(hashedPassword);
        }

        userRepository.save(user);
        return Optional.of(user);
    }

    // public void createStaff(CreateUserRequest request, User ownerUser) {
    // if (ownerUser.getRole() != Role.OWNER) {
    // throw new AccessDeniedException("Solo los OWNER pueden crear usuarios");
    // }

    // User staff = new User();
    // staff.setName(request.getName());
    // staff.setEmail(request.getEmail());
    // staff.setPassword(request.getPassword());
    // staff.setRole(Role.STAFF);
    // staff.setBusiness(ownerUser.getBusiness());

    // userRepository.save(staff);
    // }
}
