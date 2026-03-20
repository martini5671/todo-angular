package org.martini.backend.model.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TaskDto(
    Long id,
    @NotBlank
    String title,
    @NotBlank
    String description,
    boolean done,
    Long userId
) {
}
