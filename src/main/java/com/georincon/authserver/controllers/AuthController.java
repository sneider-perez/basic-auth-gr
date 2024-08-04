package com.georincon.authserver.controllers;

import com.georincon.authserver.dtos.LoginRequestDto;
import com.georincon.authserver.dtos.LoginResponseDto;
import com.georincon.authserver.dtos.SignupRequestDto;
import com.georincon.authserver.dtos.SignupResponseDto;
import com.georincon.authserver.exceptions.AuthException;
import com.georincon.authserver.services.UserService;
import com.georincon.authserver.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(
            @Valid @RequestBody SignupRequestDto signupRequest) {
        return ResponseEntity.ok(userService.saveUser(signupRequest));
    }

    @PreAuthorize("hasRole('SUPER-ADMIN')")
    @PostMapping("/admin/signup")
    public ResponseEntity<SignupResponseDto> crateUserAdmin(
            @Valid @RequestBody SignupRequestDto signupRequest) {
        return ResponseEntity.ok(userService.saveAdmin(signupRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequest) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String jwt = jwtUtil.generateToken(loginRequest.getUsername(), authentication.getAuthorities().toArray()[0].toString());
            return ResponseEntity.ok(LoginResponseDto.builder().accessToken(jwt).build());
        } else {
            throw new AuthException("Usuario o clave inválido");
        }
    }

}
