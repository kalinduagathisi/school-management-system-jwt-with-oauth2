package com.example.service.impl;

import com.example.constants.ApplicationConstants;
import com.example.dto.StudentDto;
import com.example.dto.requestDto.AddStudentRequestDto;
import com.example.dto.requestDto.UpdateStudentRequestDto;
import com.example.entity.PaymentSchemeEntity;
import com.example.entity.StudentEntity;
import com.example.exception.PaymentSchemeException;
import com.example.exception.StudentException;
import com.example.mapper.Mapper;
import com.example.repository.PaymentSchemeRepository;
import com.example.repository.StudentRepository;
import com.example.service.PaymentSchemeService;
import com.example.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PaymentSchemeRepository paymentSchemeRepository;
    private final PaymentSchemeService paymentSchemeService;
    private final Mapper mapper;

    // add new student
    @Override
    @Transactional
    public Boolean addStudent(AddStudentRequestDto addStudentRequestDto) {

        try {
            Optional<StudentEntity> byStudentEmail = studentRepository.findByEmail(addStudentRequestDto.getEmail());
            Optional<PaymentSchemeEntity> bySchemeName = paymentSchemeRepository.findBySchemeName(addStudentRequestDto.getPayment_scheme_name());

            // check if a student with entered email already exists
            if (byStudentEmail.isPresent()){
                throw new StudentException(ApplicationConstants.RESOURCE_ALREADY_EXIST, "Email already exist");
            }

            // check if payment scheme exists with given Id
            if (!bySchemeName.isPresent()){
                throw new PaymentSchemeException(ApplicationConstants.RESOURCE_NOT_FOUND, "Payment scheme with given name doesn't exists");
            }

            PaymentSchemeEntity paymentScheme = bySchemeName.get(); // Retrieve the PaymentSchemeEntity
//            PaymentSchemeEntity paymentSchemeEntity = paymentSchemeService.getSchemeBySchemeName(addStudentRequestDto.getPayment_scheme_name());
            //PaymentSchemeEntity paymentScheme = mapper.paymentSchemeDtoToPaymentScheme(paymentSchemeService.getSchemeBySchemeName(addStudentRequestDto.getPayment_scheme_name()));

            StudentEntity studentEntity = new StudentEntity(
                    addStudentRequestDto.getFirstName(),
                    addStudentRequestDto.getLastName(),
                    addStudentRequestDto.getEmail(),
                    addStudentRequestDto.getDateOfBirth(),
                    addStudentRequestDto.getStudentStatus()
            );
            studentEntity.setPaymentSchemeEntity(paymentScheme); // Assign the retrieved PaymentSchemeEntity

            studentRepository.save(studentEntity);

            return true;

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
                        byStudentEmail.get().getStudentStatus(),
                        byStudentEmail.get().getPaymentSchemeEntity()
                        );
            }
           
            
        }catch (Exception e){
            log.error("Method getStudentByEmail : "+ e.getMessage(), e);
            throw e;
        }
        
    }
}
