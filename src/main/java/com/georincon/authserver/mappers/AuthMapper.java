package com.georincon.authserver.mappers;

import com.georincon.authserver.dtos.SignupRequestDto;
import com.georincon.authserver.dtos.SignupResponseDto;
import com.georincon.authserver.entities.UserEntity;

public class AuthMapper {

    public static UserEntity signupDtoToUserEntity(SignupRequestDto signupRequest) {
        return UserEntity.builder()
                .email(signupRequest.getEmail())
                .username(signupRequest.getUsername())
                .password(signupRequest.getPassword())
                .names(signupRequest.getNames())
                .surnames(signupRequest.getSurnames())
                .build();
    }

    public static SignupResponseDto userEntityToSignupDto(UserEntity user) {
        return SignupResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .names(user.getNames())
                .surnames(user.getSurnames())
                .build();
    }

}
