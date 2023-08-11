package com.example.dto.requestDto;

import com.example.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequestDto {

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String email; // username

    @NotNull
    private String password;

    @NotNull
    private Role role;
}
