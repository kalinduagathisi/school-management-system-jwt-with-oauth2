package com.example.controller;

import com.example.dto.StudentDto;
import com.example.dto.requestDto.AddStudentRequestDto;
import com.example.dto.responseDto.CommonResponseDTO;
import com.example.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/add")
    public ResponseEntity addStudent (@RequestBody @Valid AddStudentRequestDto addStudentRequestDto){
        boolean result = studentService.addStudent(addStudentRequestDto);
        return new ResponseEntity<>(
                new CommonResponseDTO(result, "New student successfully registered..", null), HttpStatus.OK
        );
    }

    @GetMapping("/get-student/{email}")
    public ResponseEntity getStudentDetailsByEmail(@PathVariable String email){
        StudentDto student = studentService.getStudentByEmail(email);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Student found!", student), HttpStatus.OK);

    }


}
