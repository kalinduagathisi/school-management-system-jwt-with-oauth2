package com.example.dto.requestDto;

import com.example.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddStudentRequestDto {

    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private StudentStatus studentStatus;

    @NotNull
    private String payment_scheme_name;
}
