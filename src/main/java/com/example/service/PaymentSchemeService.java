package com.example.service;

import com.example.dto.PaymentSchemeDto;
import com.example.dto.requestDto.AddPaymentSchemeRequestDto;
import com.example.entity.PaymentSchemeEntity;

import java.util.List;

public interface PaymentSchemeService {
    Boolean addPaymentScheme(AddPaymentSchemeRequestDto addPaymentSchemeRequestDto);
    PaymentSchemeDto getSchemeBySchemeName(String schemeName);
    List<PaymentSchemeDto> getAllPaymentSchemes();
}
