package com.stackbytes.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InsertImageResponseDto {
    private boolean success;
    private String message;
}
