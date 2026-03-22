package org.martini.backend.model;

import jakarta.validation.constraints.NotBlank;

public record EmailDto(
        @NotBlank
        String from,
        @NotBlank
        String to,
        @NotBlank
        String subject,
        @NotBlank
        String content) {
}
