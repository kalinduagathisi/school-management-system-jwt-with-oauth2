package com.example.dto.requestDto;

import com.example.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequestDto {

    private String firstname;

    private String lastname;

    private String email; // username

    private String password;

    private Role role;
}
