package com.example.controller;

import com.example.dto.StudentDto;
import com.example.dto.requestDto.AddStudentRequestDto;
import com.example.dto.requestDto.UpdateStudentRequestDto;
import com.example.dto.responseDto.CommonResponseDTO;
import com.example.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/students")
@RequiredArgsConstructor
public class StudentDivisionController {

    private final StudentService studentService;

    @PostMapping("/add")
    public ResponseEntity addStudent(@RequestBody @Valid AddStudentRequestDto addStudentRequestDto) {
        boolean result = studentService.addStudent(addStudentRequestDto);
        return new ResponseEntity<>(
                new CommonResponseDTO(result, "New student successfully registered..", null), HttpStatus.OK
        );
    }

    @GetMapping("/get-student/{email}")
    public ResponseEntity getStudentDetailsByEmail(@PathVariable String email) {
        StudentDto student = studentService.getStudentByEmail(email);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Student found!", student), HttpStatus.OK);

    }

    @GetMapping("/get-all-students")
    public ResponseEntity getAllStudents() {
        List<StudentDto> allStudents = studentService.getAllStudents();
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "All Students loaded!", allStudents), HttpStatus.OK);

    }


    @GetMapping("/get-students/filter")
    public ResponseEntity getStudentsByBirthMonthAndYear(
            @RequestParam int birthMonth,
            @RequestParam int birthYear
    ) {
        List<StudentDto> filteredStudents = studentService.getStudentsByBirthMonthAndYear(birthMonth, birthYear);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Filtered Students loaded!", filteredStudents), HttpStatus.OK);

    }

    @PutMapping("/update")
    public ResponseEntity updateStudent(@RequestBody @Valid UpdateStudentRequestDto updateStudentRequestDto) {
        boolean result = studentService.updateStudent(updateStudentRequestDto);
        return new ResponseEntity<>(
                new CommonResponseDTO(result, "Student successfully updated..", null), HttpStatus.OK
        );
    }


}
