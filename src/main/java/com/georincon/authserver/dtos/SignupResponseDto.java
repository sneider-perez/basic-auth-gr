package com.georincon.authserver.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponseDto {

    private String username;
    private String email;
    private String names;
    private String surnames;

}
