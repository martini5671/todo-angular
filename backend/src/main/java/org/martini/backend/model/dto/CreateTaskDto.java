package org.martini.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateTaskDto(
    @NotBlank
    String title,
    String description
) {
}
