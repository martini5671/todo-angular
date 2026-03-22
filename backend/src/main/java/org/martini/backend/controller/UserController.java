package org.martini.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.martini.backend.model.dto.AuthenticationResponseDto;
import org.martini.backend.model.dto.LoginDto;
import org.martini.backend.model.dto.RegistrationDto;
import org.martini.backend.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public AuthenticationResponseDto login(@RequestBody @Valid LoginDto loginDto) {
        return userService.login(loginDto);
    }

    @PostMapping("/registration")
    public void register(@RequestBody @Valid RegistrationDto registrationDto) {
        userService.register(registrationDto);
    }


}
