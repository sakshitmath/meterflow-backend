package com.meterflow.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateApiRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String baseUrl;

    private String description;
}