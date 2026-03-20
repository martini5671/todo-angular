package org.martini.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateTaskDto(
    @NotBlank
    String title,
    String description,
    boolean done
) {
}
