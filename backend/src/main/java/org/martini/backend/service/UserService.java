package org.martini.backend.service;

import lombok.RequiredArgsConstructor;
import org.martini.backend.model.dto.AuthenticationResponseDto;
import org.martini.backend.model.dto.LoginDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public AuthenticationResponseDto login(LoginDto loginDTO) {
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
      loginDTO.username(),
      loginDTO.password()
    ));

    String token = jwtService.generateToken(loginDTO.username(),
      authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList()
    );

    return AuthenticationResponseDto.builder()
      .token(token)
      .roles(authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList())
      .build();
  }
}
