package org.martini.backend.service;

import lombok.RequiredArgsConstructor;
import org.martini.backend.exception.UserAlreadyExistsException;
import org.martini.backend.model.dao.Role;
import org.martini.backend.model.dao.User;
import org.martini.backend.model.dto.AuthenticationResponseDto;
import org.martini.backend.model.dto.LoginDto;
import org.martini.backend.model.dto.RegistrationDto;
import org.martini.backend.repository.RoleRepository;
import org.martini.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

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

    @Transactional
    public void register(RegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.email())) {
            throw new UserAlreadyExistsException();
        }
        userRepository.save(User.builder()
                .userRoles(List.of(
                        roleRepository.findByName(Role.ROLE_USER.getName())
                                .orElseThrow()
                ))
                .username(registrationDto.email())
                .password(passwordEncoder.encode(registrationDto.password()))
                .isEnabled(false)
                .build());
    }
}
