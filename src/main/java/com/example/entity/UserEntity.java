package com.example.entity;

import com.example.enums.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class UserEntity {

    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @Column(unique = true)
    @Email(message = "Please enter valid email address!")
    private String email;
    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public UserEntity(String firstname, String lastname, String email, String password, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
