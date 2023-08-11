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

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentSchemeServiceImpl implements PaymentSchemeService {

    private final PaymentSchemeRepository paymentSchemeRepository;
    private final Mapper mapper;


    // add payment scheme
    @Override
    @Transactional
    public Boolean addPaymentScheme(AddPaymentSchemeRequestDto addPaymentSchemeRequestDto) {

        try {
            Optional<PaymentSchemeEntity> byPaymentSchemeName = paymentSchemeRepository.findBySchemeName(addPaymentSchemeRequestDto.getSchemeName());

            // check if the scheme name already exists
            if (byPaymentSchemeName.isPresent()){
                throw new PaymentSchemeException(ApplicationConstants.RESOURCE_ALREADY_EXIST, "Scheme name already exist");
            }

            PaymentSchemeEntity scheme = new PaymentSchemeEntity();

            // Set properties from the request DTO to the entity
            scheme.setSchemeName(addPaymentSchemeRequestDto.getSchemeName());
            scheme.setSchemeType(addPaymentSchemeRequestDto.getSchemeType());

            List<Map<String, Object>> paymentPlanEntityList = addPaymentSchemeRequestDto.getPaymentPlanEntityList();
            if (paymentPlanEntityList != null) {
                for (Map<String, Object> paymentPlanData : paymentPlanEntityList) {
                    String feeType = (String) paymentPlanData.get("feeType");
                    BigDecimal amount = BigDecimal.valueOf((Double) paymentPlanData.get("amount"));

                    PaymentPlanEntity paymentPlanEntity = new PaymentPlanEntity();
                    paymentPlanEntity.setFeeType(feeType);
                    paymentPlanEntity.setAmount(amount);
                    paymentPlanEntity.setPaymentSchemeEntity(scheme);

                    // Add the payment plan entity to the payment scheme entity
                    scheme.addPaymentPlanEntity(paymentPlanEntity);
                }
            }
            // Save the payment scheme entity along with associated payment plans
            PaymentSchemeEntity savedPaymentScheme = paymentSchemeRepository.save(scheme);

            return true;

        }catch (Exception e){
            log.error("Method addPaymentScheme : "+ e.getMessage(), e);
            throw e;
        }

    }

    // get payment scheme by name
    @Override
    public PaymentSchemeDto getSchemeBySchemeName(String schemeName) {
        try {
            Optional<PaymentSchemeEntity> bySchemeName = paymentSchemeRepository.findBySchemeName(schemeName);

            if (!bySchemeName.isPresent()){
                throw new PaymentSchemeException(ApplicationConstants.RESOURCE_NOT_FOUND, "Scheme with name = " + schemeName+ " not found!");
            }

            else {
                PaymentSchemeEntity paymentSchemeEntity = bySchemeName.get();

                List<PaymentPlanDto> paymentPlanDtoList = paymentSchemeEntity.getPaymentPlanEntity().stream()
                        .map(this::mapToPaymentPlanDto)
                        .collect(Collectors.toList());

                return new PaymentSchemeDto(
                        paymentSchemeEntity.getSchemeId(),
                        paymentSchemeEntity.getSchemeName(),
                        paymentSchemeEntity.getSchemeType(),
                        paymentPlanDtoList
                );
            }


        }catch (Exception e){
            log.error("Method getSchemeBySchemeName : "+ e.getMessage(), e);
            throw e;
        }
    }

    private PaymentPlanDto mapToPaymentPlanDto(PaymentPlanEntity paymentPlanEntity) {
        return new PaymentPlanDto(
                paymentPlanEntity.getPaymentPlanId(),
                paymentPlanEntity.getFeeType(),
                paymentPlanEntity.getAmount()
        );
    }

    // get all schemes
    @Override
    public List<PaymentSchemeDto> getAllPaymentSchemes() {

        log.info("Execute method getAllPaymentSchemes : ");

        List<PaymentSchemeEntity> paymentSchemes = paymentSchemeRepository.findAll();
        List<PaymentSchemeDto> paymentSchemeDtos = new ArrayList<>();


        for (PaymentSchemeEntity paymentScheme: paymentSchemes){

            List<PaymentPlanDto> paymentPlanDtoList = paymentScheme.getPaymentPlanEntity().stream()
                    .map(this::mapToPaymentPlanDto)
                    .collect(Collectors.toList());

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
