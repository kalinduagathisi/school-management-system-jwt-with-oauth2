package com.example.dto;

import com.example.entity.PaymentSchemeEntity;
import com.example.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private Integer studentId;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate dateOfBirth;

    private StudentStatus studentStatus;

    private PaymentSchemeEntity payment_scheme_Id;
}
