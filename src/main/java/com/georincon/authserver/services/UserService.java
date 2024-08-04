package com.georincon.authserver.services;

import com.georincon.authserver.dtos.SignupRequestDto;
import com.georincon.authserver.dtos.SignupResponseDto;
import com.georincon.authserver.entities.UserEntity;
import com.georincon.authserver.exceptions.AuthException;
import com.georincon.authserver.exceptions.ResourceAlreadyExistsException;
import com.georincon.authserver.mappers.AuthMapper;
import com.georincon.authserver.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto saveUser(SignupRequestDto signupRequestDto) {
        UserEntity user = AuthMapper.signupDtoToUserEntity(signupRequestDto);
        user.setRole("USER");
        return save(user);
    }

    public SignupResponseDto saveAdmin(SignupRequestDto signupRequestDto) {
        UserEntity user = AuthMapper.signupDtoToUserEntity(signupRequestDto);
        user.setRole("ADMIN");
        return save(user);
    }

    private SignupResponseDto save(UserEntity user) {
        Optional<UserEntity> byEmailOrUsername = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());
        if (byEmailOrUsername.isPresent()) {
            throw new ResourceAlreadyExistsException("Usuario o email ya registrado en el sistema");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return AuthMapper.userEntityToSignupDto(userRepository.save(user));
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = findByUsername(username);
        if (user.isEmpty()) {
            throw new AuthException("Usuario o correo no existe");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(String.format("ROLE_%s", user.get().getRole()));
        return new User(user.get().getUsername(), user.get().getPassword(), List.of(authority));
    }

}

