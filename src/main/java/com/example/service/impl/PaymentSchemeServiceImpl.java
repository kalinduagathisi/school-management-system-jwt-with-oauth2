package com.example.service.impl;

import com.example.constants.ApplicationConstants;
import com.example.dto.PaymentPlanDto;
import com.example.dto.PaymentSchemeDto;
import com.example.dto.requestDto.AddPaymentSchemeRequestDto;
import com.example.entity.PaymentPlanEntity;
import com.example.entity.PaymentSchemeEntity;
import com.example.exception.PaymentSchemeException;
import com.example.mapper.Mapper;
import com.example.repository.PaymentSchemeRepository;
import com.example.service.PaymentSchemeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Declare the class as a service
@Service
// Enable logging for the class
@Log4j2
// Enable transaction management
@Transactional
// Use Lombok to generate constructor injection
@RequiredArgsConstructor
public class PaymentSchemeServiceImpl implements PaymentSchemeService {

    // Inject PaymentSchemeRepository dependency
    private final PaymentSchemeRepository paymentSchemeRepository;
    // Inject Mapper dependency
    private final Mapper mapper;

    // Implement the method to add a new payment scheme
    @Override
    public Boolean addPaymentScheme(AddPaymentSchemeRequestDto addPaymentSchemeRequestDto) {

        try {
            // Check if the scheme name already exists
            Optional<PaymentSchemeEntity> byPaymentSchemeName = paymentSchemeRepository.findBySchemeName(addPaymentSchemeRequestDto.getSchemeName());
            if (byPaymentSchemeName.isPresent()) {
                throw new PaymentSchemeException(ApplicationConstants.RESOURCE_ALREADY_EXIST, "Scheme name already exists.");
            }

            // Create a new PaymentSchemeEntity
            PaymentSchemeEntity scheme = new PaymentSchemeEntity();
            scheme.setSchemeName(addPaymentSchemeRequestDto.getSchemeName());
            scheme.setSchemeType(addPaymentSchemeRequestDto.getSchemeType());

            // Process payment plans
            List<Map<String, Object>> paymentPlanEntityList = addPaymentSchemeRequestDto.getPaymentPlanEntityList();
            if (paymentPlanEntityList != null) {
                for (Map<String, Object> paymentPlanData : paymentPlanEntityList) {
                    String feeType = (String) paymentPlanData.get("feeType");
                    BigDecimal amount = BigDecimal.valueOf((Double) paymentPlanData.get("amount"));

                    // Create a new PaymentPlanEntity
                    PaymentPlanEntity paymentPlanEntity = new PaymentPlanEntity();
                    paymentPlanEntity.setFeeType(feeType);
                    paymentPlanEntity.setAmount(amount);
                    paymentPlanEntity.setPaymentSchemeEntity(scheme);

                    // Add the payment plan entity to the payment scheme entity
                    scheme.addPaymentPlanEntity(paymentPlanEntity);
                }
            }

            // Save the payment scheme entity along with associated payment plans
            paymentSchemeRepository.save(scheme);

            return true;

        } catch (Exception e) {
            // Log and handle any exceptions
            log.error("Method addPaymentScheme : " + e.getMessage(), e);
            throw e;
        }
    }

    // Implement the method to get payment scheme by name
    @Override
    public PaymentSchemeDto getSchemeBySchemeName(String schemeName) {
        try {
            // Check if a payment scheme with the given name exists
            Optional<PaymentSchemeEntity> bySchemeName = paymentSchemeRepository.findBySchemeName(schemeName);

            if (!bySchemeName.isPresent()) {
                throw new PaymentSchemeException(ApplicationConstants.RESOURCE_NOT_FOUND, "Scheme with name = " + schemeName + " not found.");
            } else {
                // Retrieve PaymentSchemeEntity and map its payment plans to DTOs
                PaymentSchemeEntity paymentSchemeEntity = bySchemeName.get();
                List<PaymentPlanDto> paymentPlanDtoList = paymentSchemeEntity.getPaymentPlanEntity().stream()
                        .map(this::mapToPaymentPlanDto)
                        .collect(Collectors.toList());

                // Create and return PaymentSchemeDto
                return new PaymentSchemeDto(
                        paymentSchemeEntity.getSchemeId(),
                        paymentSchemeEntity.getSchemeName(),
                        paymentSchemeEntity.getSchemeType(),
                        paymentPlanDtoList
                );
            }
        } catch (Exception e) {
            // Log and handle any exceptions
            log.error("Method getSchemeBySchemeName : " + e.getMessage(), e);
            throw e;
        }
    }

    // Helper method to map PaymentPlanEntity to PaymentPlanDto
    private PaymentPlanDto mapToPaymentPlanDto(PaymentPlanEntity paymentPlanEntity) {
        return new PaymentPlanDto(
                paymentPlanEntity.getPaymentPlanId(),
                paymentPlanEntity.getFeeType(),
                paymentPlanEntity.getAmount()
        );
    }

    // Implement the method to get details of all payment schemes
    @Override
    public List<PaymentSchemeDto> getAllPaymentSchemes() {
        log.info("Execute method getAllPaymentSchemes : ");

        // Retrieve all payment scheme entities
        List<PaymentSchemeEntity> paymentSchemes = paymentSchemeRepository.findAll();
        List<PaymentSchemeDto> paymentSchemeDtos = new ArrayList<>();

        // Map payment plans of each scheme to DTOs
        for (PaymentSchemeEntity paymentScheme : paymentSchemes) {
            List<PaymentPlanDto> paymentPlanDtoList = paymentScheme.getPaymentPlanEntity().stream()
                    .map(this::mapToPaymentPlanDto)
                    .collect(Collectors.toList());

            // Create and add PaymentSchemeDto to the list
            paymentSchemeDtos.add(
                    new PaymentSchemeDto(
                            paymentScheme.getSchemeId(),
                            paymentScheme.getSchemeName(),
                            paymentScheme.getSchemeType(),
                            paymentPlanDtoList
                    )
            );
        }
        return paymentSchemeDtos;
    }
}
