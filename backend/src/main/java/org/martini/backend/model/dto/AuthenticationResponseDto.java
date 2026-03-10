package org.martini.backend.model.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AuthenticationResponseDto(
  String token,
  List<String> roles
) {
}
