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

import java.util.*;

// Declare the class as a service
@Service
// Enable logging for the class
@Log4j2
// Enable transaction management
@Transactional
// Use Lombok to generate constructor injection
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    // Inject StudentRepository dependency
    private final StudentRepository studentRepository;
    // Inject PaymentSchemeRepository dependency
    private final PaymentSchemeRepository paymentSchemeRepository;
    // Inject PaymentSchemeService dependency
    private final PaymentSchemeService paymentSchemeService;
    // Inject Mapper dependency
    private final Mapper mapper;

    // Implement the method to add a new student
    @Override
    public Boolean addStudent(AddStudentRequestDto addStudentRequestDto) {

        try {
            // Check if a student with the entered email already exists
            Optional<StudentEntity> byStudentEmail = studentRepository.findByEmail(addStudentRequestDto.getEmail());
            // Check if payment scheme exists with the given name
            Optional<PaymentSchemeEntity> bySchemeName = paymentSchemeRepository.findBySchemeName(addStudentRequestDto.getPayment_scheme_name());

            if (byStudentEmail.isPresent()) {
                throw new StudentException(ApplicationConstants.RESOURCE_ALREADY_EXIST, "Email already exists.");
            }

            if (!bySchemeName.isPresent()) {
                throw new PaymentSchemeException(ApplicationConstants.RESOURCE_NOT_FOUND, "Payment scheme with given name doesn't exist.");
            }

            // Retrieve the PaymentSchemeEntity
            PaymentSchemeEntity paymentScheme = bySchemeName.get();

            // Create StudentEntity and associate it with the PaymentSchemeEntity
            StudentEntity studentEntity = new StudentEntity(
                    addStudentRequestDto.getFirstName(),
                    addStudentRequestDto.getLastName(),
                    addStudentRequestDto.getEmail(),
                    addStudentRequestDto.getDateOfBirth(),
                    addStudentRequestDto.getStudentStatus()
            );
            studentEntity.setPaymentSchemeEntity(paymentScheme);

            studentRepository.save(studentEntity);

            return true;

        } catch (Exception e) {
            // Log and handle any exceptions
            log.error("Method addStudent : " + e.getMessage(), e);
            throw e;
        }
    }

    // Implement the method to update student details
    @Override
    public Boolean updateStudent(UpdateStudentRequestDto updateStudentRequestDto) {

        log.info("Execute method updateStudent : updateStudentRequestDto : " + updateStudentRequestDto.toString());

        try {
            // Check if a student exists with the given ID
            Optional<StudentEntity> byStudentId = studentRepository.findById(updateStudentRequestDto.getStudentId());
            if (!byStudentId.isPresent()) {
                throw new StudentException(ApplicationConstants.RESOURCE_NOT_FOUND, "Student with given ID not found.");
            }

            // Check if payment scheme exists with the given name
            Optional<PaymentSchemeEntity> bySchemeName = paymentSchemeRepository.findBySchemeName(updateStudentRequestDto.getPayment_scheme_name());
            if (!bySchemeName.isPresent()) {
                throw new PaymentSchemeException(ApplicationConstants.RESOURCE_NOT_FOUND, "Payment scheme not found.");
            }

            // Retrieve the StudentEntity and PaymentSchemeEntity
            StudentEntity studentEntity = byStudentId.get();
            PaymentSchemeEntity paymentScheme = bySchemeName.get();

            // Check whether the email already exists for another student
            Optional<StudentEntity> byStudentEmail = studentRepository.findByEmail(updateStudentRequestDto.getEmail());
            if (byStudentEmail.isPresent() && studentEntity.getStudentId() != byStudentEmail.get().getStudentId()) {
                throw new StudentException(ApplicationConstants.RESOURCE_ALREADY_EXIST, "Student with given email already exists.");
            }

            // Update student details and assign the PaymentSchemeEntity
            studentEntity.setFirstName(updateStudentRequestDto.getFirstName());
            studentEntity.setLastName(updateStudentRequestDto.getLastName());
            studentEntity.setEmail(updateStudentRequestDto.getEmail());
            studentEntity.setDateOfBirth(updateStudentRequestDto.getDateOfBirth());
            studentEntity.setStudentStatus(updateStudentRequestDto.getStudentStatus());
            studentEntity.setPaymentSchemeEntity(paymentScheme);

            studentRepository.save(studentEntity);

            return true;

        } catch (Exception e) {
            // Log and handle any exceptions
            log.error("Method updateStudent : " + e.getMessage(), e);
            throw e;
        }
    }

    // Implement the method to get student by email
    @Override
    public StudentDto getStudentByEmail(String email) {
        try {
            // Check if a student with the given email exists
            Optional<StudentEntity> byStudentEmail = studentRepository.findByEmail(email);

            if (!byStudentEmail.isPresent()) {
                throw new StudentException(ApplicationConstants.RESOURCE_NOT_FOUND, "Student with email = " + email + " not found.");
            } else {
                // Create and return a StudentDto
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
        } catch (Exception e) {
            // Log and handle any exceptions
            log.error("Method getStudentByEmail : " + e.getMessage(), e);
            throw e;
        }
    }

    // Implement the method to get details of all students
    @Override
    public List<StudentDto> getAllStudents() {
        log.info("Execute method getAllStudents : ");

        // Retrieve all student entities
        List<StudentEntity> allStudents = studentRepository.findAll();
        List<StudentDto> allStudentsToBeGet = new ArrayList<>();

        // Convert student entities to DTOs for response
        for (StudentEntity student : allStudents) {
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

    // Implement the method to get students by birth month and year
    @Override
    public List<StudentDto> getStudentsByBirthMonthAndYear(int birthMonth, int birthYear) {
        log.info("Execute method getStudentsByBirthMonthAndYear : ");

        // Validate birth month (between 1 and 12 inclusive)
        if (birthMonth < 1 || birthMonth > 12) {
            throw new IllegalArgumentException("Birth month should be between 1 and 12.");
        }

        // Validate birth year (between 1950 and current year)
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (birthYear < 1950 || birthYear > currentYear) {
            throw new IllegalArgumentException("Birth year should be between 1950 and the current year.");
        }

        // Retrieve student entities by birth month and year
        List<StudentEntity> allStudents = studentRepository.findByBirthMonthAndYear(birthMonth, birthYear);
        List<StudentDto> allStudentsToBeGet = new ArrayList<>();

        // Convert student entities to DTOs for response
        for (StudentEntity student : allStudents) {
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

    // Implement the method to get students by birthdate range
    @Override
    public List<StudentDto> getStudentsByBirthdateRange(Date startDate, Date endDate) {
        // Log the execution of the method
        log.info("Execute method getStudentsByBirthdateRange : ");

        // Check if the input dates are valid
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            throw new IllegalArgumentException("Invalid date range.");
        }

        // Define the minimum start date for comparison
        Calendar calendar = Calendar.getInstance();
        calendar.set(1950, Calendar.JANUARY, 1);
        Date minStartDate = calendar.getTime();

        // Check if the start date is valid
        if (startDate.before(minStartDate)) {
            throw new IllegalArgumentException("Start date should be after January 1, 1950.");
        }

        // Retrieve student entities within the specified birthdate range
        List<StudentEntity> allStudents = studentRepository.findByBirthdateBetween(startDate, endDate);

        // Convert student entities to DTOs for response
        List<StudentDto> allStudentsToBeGet = new ArrayList<>();
        for (StudentEntity student : allStudents) {
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

        // Return the list of StudentDto objects within the specified birthdate range
        return allStudentsToBeGet;
    }
}
