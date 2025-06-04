package com.example.stocker.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stocker.dto.BusinessPatchDTO;
import com.example.stocker.entity.Business;
import com.example.stocker.security.JwtService;
import com.example.stocker.service.BusinessService;

@RestController
@RequestMapping("/business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private JwtService jwtService;

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchBusiness(@PathVariable Long id, @RequestBody BusinessPatchDTO dto) {
        try {
            Optional<Business> updatedBusinessOpt = businessService.updatePartial(id, dto);
            if (updatedBusinessOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Business updatedBusiness = updatedBusinessOpt.get();
            String newToken = jwtService.generateToken(updatedBusiness.getName());

            Map<String, Object> response = new HashMap<>();
            response.put("user", updatedBusiness);
            response.put("token", newToken);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
