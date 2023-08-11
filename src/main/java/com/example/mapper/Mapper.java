package com.example.mapper;


import com.example.dto.PaymentSchemeDto;
import com.example.entity.PaymentSchemeEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final ModelMapper modelMapper;

    // paymentScheme to paymentSchemeDto
    public PaymentSchemeDto paymentSchemeToPaymentSchemeDto(PaymentSchemeEntity paymentScheme) {

        PaymentSchemeDto paymentSchemeDto = modelMapper.map(paymentScheme, PaymentSchemeDto.class);
        return paymentSchemeDto;
    }

    // paymentSchemeDto to paymentScheme
    public PaymentSchemeEntity paymentSchemeDtoToPaymentScheme(PaymentSchemeDto paymentSchemeDto) {

        PaymentSchemeEntity paymentScheme = modelMapper.map(paymentSchemeDto, PaymentSchemeEntity.class);
        return paymentScheme;
    }


}
