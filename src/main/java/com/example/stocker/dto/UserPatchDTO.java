package com.example.stocker.dto;

import lombok.Data;

@Data
public class UserPatchDTO {
    private String name;
    private String email;
    private String password;
}
