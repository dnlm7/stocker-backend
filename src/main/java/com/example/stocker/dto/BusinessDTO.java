package com.example.stocker.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BusinessDTO {
    private Long id;
    private String name;
    private String ticker;
    private String sector;
    private String industry;
    private String description;
}
