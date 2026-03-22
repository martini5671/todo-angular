package org.martini.backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TaskDto(
        @NotNull
        Long id,
        @NotBlank
        String title,
        String description,
        @NotNull
        Boolean done,
        @NotNull
        Long userId
) {
}
