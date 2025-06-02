package com.example.stocker.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stocker.dto.UserPatchDTO;
import com.example.stocker.entity.User;
import com.example.stocker.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

    public Optional<User> updatePartial(Long id, UserPatchDTO dto) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();

        // Validar email solo si es distinto al actual
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            String newEmail = dto.getEmail();

            // Evitar conflictos con otros usuarios
            Optional<User> existingByEmail = userRepository.findByEmail(newEmail);
            if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(user.getId())) {
                throw new IllegalArgumentException("El email ya está en uso por otro usuario");
            }

            user.setEmail(newEmail);
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(dto.getPassword()); // Recuerda encriptarlo si aplica
        }

        return Optional.of(userRepository.save(user));
    }

}
