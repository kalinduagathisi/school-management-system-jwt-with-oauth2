package com.example.service.impl;

import com.example.constants.ApplicationConstants;
import com.example.dto.StudentDto;
import com.example.dto.requestDto.AddStudentRequestDto;
import com.example.dto.requestDto.UpdateStudentRequestDto;
import com.example.entity.StudentEntity;
import com.example.exception.StudentException;
import com.example.repository.StudentRepository;
import com.example.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    // add new student
    @Override
    public Boolean addStudent(AddStudentRequestDto addStudentRequestDto) {

        try {
            Optional<StudentEntity> byStudentEmail = studentRepository.findByEmail(addStudentRequestDto.getEmail());

            // check if a student with entered email already exists
            if (byStudentEmail.isPresent()){
                throw new StudentException(ApplicationConstants.RESOURCE_ALREADY_EXIST, "Email already exist");
            }
            else {
                studentRepository.save(
                        new StudentEntity(
                                addStudentRequestDto.getFirstName(),
                                addStudentRequestDto.getLastName(),
                                addStudentRequestDto.getEmail(),
                                addStudentRequestDto.getDateOfBirth(),
                                addStudentRequestDto.getStudentStatus()
                        )
                );
                return true;
            }
        }catch (Exception e){
            log.error("Method addStudent : "+ e.getMessage(), e);
            throw e;
        }


    }

    @Override
    public Boolean updateStudent(UpdateStudentRequestDto updateStudentRequestDto) {
        // TODO: 10-Aug-23  
        return null;
    }

    
    // get student by email
    @Override
    public StudentDto getStudentByEmail(String email) {
        try {
            Optional<StudentEntity> byStudentEmail = studentRepository.findByEmail(email);
            
            if (!byStudentEmail.isPresent()){
                throw new StudentException(ApplicationConstants.RESOURCE_NOT_FOUND, "Student with email = " + email+ " not found!");
            }
            
            else {
                return new StudentDto(
                        byStudentEmail.get().getStudentId(),
                        byStudentEmail.get().getFirstName(),
                        byStudentEmail.get().getLastName(),
                        byStudentEmail.get().getEmail(),
                        byStudentEmail.get().getDateOfBirth(),
                        byStudentEmail.get().getStudentStatus()
                        );
            }
           
            
        }catch (Exception e){
            log.error("Method getStudentByEmail : "+ e.getMessage(), e);
            throw e;
        }
        
    }
}
