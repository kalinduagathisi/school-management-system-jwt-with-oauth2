package com.example.dto.requestDto;

import com.example.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentRequestDto {

    @NotNull
    private Integer studentId;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private String payment_scheme_name;

    @NotNull
    private StudentStatus studentStatus;
}
