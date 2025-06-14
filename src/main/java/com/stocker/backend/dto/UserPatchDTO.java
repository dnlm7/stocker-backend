package com.stocker.backend.dto;

import lombok.Data;

@Data
public class UserPatchDTO {
    private String name;
    private String email;
    private String password;
}
