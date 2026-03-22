package org.martini.backend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record RegistrationDto(
        @Email
        String email,
        @NotEmpty
        String password
) {
}
