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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
    @Transactional
    public Boolean updateStudent(UpdateStudentRequestDto updateStudentRequestDto) {

        log.info("Execute method updateStudent : updateStudentRequestDto : " + updateStudentRequestDto.toString());

        try {
            Optional<StudentEntity> byStudentEmail = studentRepository.findByEmail(updateStudentRequestDto.getEmail());
            Optional<StudentEntity> byStudentId = studentRepository.findById(updateStudentRequestDto.getStudentId());

            Optional<PaymentSchemeEntity> bySchemeName = paymentSchemeRepository.findBySchemeName(updateStudentRequestDto.getPayment_scheme_name());

            // check if a student exists with the given ID
            if (!byStudentId.isPresent()){
                throw new StudentException(ApplicationConstants.RESOURCE_NOT_FOUND, "Student with given ID not found!");
            }

            if (!bySchemeName.isPresent()){
                throw new PaymentSchemeException(ApplicationConstants.RESOURCE_NOT_FOUND, "Payment scheme not found!");
            }

            StudentEntity studentEntity = byStudentId.get();
            PaymentSchemeEntity paymentScheme = bySchemeName.get(); // Retrieve the PaymentSchemeEntity

            // check whether same student email
            if (byStudentEmail.isPresent()){
                if (studentEntity.getStudentId() != byStudentEmail.get().getStudentId()){
                    throw new StudentException(ApplicationConstants.RESOURCE_ALREADY_EXIST, "Student with given email already exists!");
                }
            }

            studentEntity.setFirstName(updateStudentRequestDto.getFirstName());
            studentEntity.setLastName(updateStudentRequestDto.getLastName());
            studentEntity.setEmail(updateStudentRequestDto.getEmail());
            studentEntity.setDateOfBirth(updateStudentRequestDto.getDateOfBirth());
            studentEntity.setStudentStatus(updateStudentRequestDto.getStudentStatus());
            studentEntity.setPaymentSchemeEntity(paymentScheme); // Assign the retrieved PaymentSchemeEntity

            studentRepository.save(studentEntity);

            return true;

        }catch (Exception e){
            log.error("Method updateStudent : "+ e.getMessage(), e);
            throw e;
        }
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

    @Override
    public List<StudentDto> getAllStudents() {

        log.info("Execute method getAllStudents : ");

        List<StudentEntity> allStudents = studentRepository.findAll();
        List<StudentDto> allStudentsToBeGet = new ArrayList<>();

        for (StudentEntity student: allStudents){
            allStudentsToBeGet.add(
                    new StudentDto(
                            student.getStudentId(),
                            student.getFirstName(),
                            student.getLastName(),
                            student.getEmail(),
                            student.getDateOfBirth(),
                            student.getStudentStatus(),
                            student.getPaymentSchemeEntity()
                    )
            );
        }
        return allStudentsToBeGet;
    }


    // get students by birth moth and year
    @Override
    public List<StudentDto> getStudentsByBirthMonthAndYear(int birthMonth, int birthYear) {

        log.info("Execute method getStudentsByBirthMonthAndYear : ");

        // Validate birth month (between 1 and 12 both inclusive)
        if (birthMonth <= 1 || birthMonth >= 12) {
            throw new IllegalArgumentException("Birth month should be between 1 and 12.");
        }

        // Validate birth year (between 1950 and current year)
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (birthYear < 1950 || birthYear > currentYear) {
            throw new IllegalArgumentException("Birth year should be between 1950 and the current year.");
        }

        List<StudentEntity> allStudents = studentRepository.findByBirthMonthAndYear(birthMonth, birthYear);
        List<StudentDto> allStudentsToBeGet = new ArrayList<>();

        for (StudentEntity student: allStudents){
            allStudentsToBeGet.add(
                    new StudentDto(
                            student.getStudentId(),
                            student.getFirstName(),
                            student.getLastName(),
                            student.getEmail(),
                            student.getDateOfBirth(),
                            student.getStudentStatus(),
                            student.getPaymentSchemeEntity()
                    )
            );
        }
        return allStudentsToBeGet;
    }
}
