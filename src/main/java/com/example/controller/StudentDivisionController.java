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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/students")
@RequiredArgsConstructor
public class StudentDivisionController {

    private final StudentService studentService;

    // Endpoint to add a new student
    @PostMapping("/add")
    public ResponseEntity addStudent(@RequestBody @Valid AddStudentRequestDto addStudentRequestDto) {
        boolean result = studentService.addStudent(addStudentRequestDto);
        return new ResponseEntity<>(
                new CommonResponseDTO(result, "New student successfully registered.", null), HttpStatus.OK
        );
    }

    // Endpoint to get student details by email
    @GetMapping("/get-student/{email}")
    public ResponseEntity getStudentDetailsByEmail(@PathVariable String email) {
        StudentDto student = studentService.getStudentByEmail(email);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Student found!", student), HttpStatus.OK);
    }

    // Endpoint to get details of all students
    @GetMapping("/get-all-students")
    public ResponseEntity getAllStudents() {
        List<StudentDto> allStudents = studentService.getAllStudents();
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "All Students loaded!", allStudents), HttpStatus.OK);
    }

    // Endpoint to get students filtered by birth month and year
    @GetMapping("/get-students/filter")
    public ResponseEntity getStudentsByBirthMonthAndYear(
            @RequestParam int birthMonth,
            @RequestParam int birthYear
    ) {
        List<StudentDto> filteredStudents = studentService.getStudentsByBirthMonthAndYear(birthMonth, birthYear);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Filtered Students loaded!", filteredStudents), HttpStatus.OK);
    }

    // Endpoint to get students filtered by birthdate range
    @GetMapping("/get-students/filter/range")
    public ResponseEntity getStudentsByBirthdateRange(
            @RequestParam Date startDate,
            @RequestParam Date endDate
    ) {
        List<StudentDto> filteredStudents = studentService.getStudentsByBirthdateRange(startDate, endDate);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Filtered Students loaded!", filteredStudents), HttpStatus.OK);
    }

    // Endpoint to update student details
    @PutMapping("/update")
    public ResponseEntity updateStudent(@RequestBody @Valid UpdateStudentRequestDto updateStudentRequestDto) {
        boolean result = studentService.updateStudent(updateStudentRequestDto);
        return new ResponseEntity<>(
                new CommonResponseDTO(result, "Student successfully updated.", null), HttpStatus.OK
        );
    }
}
