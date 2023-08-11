package com.example.entity;

import com.example.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "student_table")
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer studentId;

    private String firstName;

    private String lastName;

    @Email(message = "Please enter valid email address!")
    @Column(unique = true)
    private String email;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus;

//        @JsonIgnore
//    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="schemeId")
//    @JsonIdentityReference(alwaysAsId=true)  // returns only the id of the required entity
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "scheme_id")
    private PaymentSchemeEntity paymentSchemeEntity;

    public StudentEntity(String firstName, String lastName, String email, LocalDate dateOfBirth, StudentStatus studentStatus, PaymentSchemeEntity paymentSchemeEntity) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.studentStatus = studentStatus;
        this.paymentSchemeEntity = paymentSchemeEntity;
    }


    public StudentEntity(String firstName, String lastName, String email, LocalDate dateOfBirth, StudentStatus studentStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.studentStatus = studentStatus;
    }
}

