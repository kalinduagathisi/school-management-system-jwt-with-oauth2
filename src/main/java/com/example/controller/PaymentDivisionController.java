package com.example.controller;

import com.example.dto.PaymentSchemeDto;
import com.example.dto.requestDto.AddPaymentSchemeRequestDto;
import com.example.dto.responseDto.CommonResponseDTO;
import com.example.mapper.Mapper;
import com.example.service.PaymentSchemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentDivisionController {

    private final PaymentSchemeService paymentSchemeService;
    private final Mapper mapper;

    @PostMapping("/add")
    public ResponseEntity addPaymentScheme (@RequestBody @Valid AddPaymentSchemeRequestDto addPaymentSchemeRequestDto){
        boolean result = paymentSchemeService.addPaymentScheme(addPaymentSchemeRequestDto);
        return new ResponseEntity<>(
                new CommonResponseDTO(result, "New payment scheme successfully created..",null), HttpStatus.OK
        );
    }

    @GetMapping("/get-scheme/{name}")
    public ResponseEntity getPaymentSchemeByEmail(@PathVariable String name){
        PaymentSchemeDto paymentScheme = paymentSchemeService.getSchemeBySchemeName(name);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Student found!", paymentScheme), HttpStatus.OK);

    }

    @GetMapping("/get-all-schemes")
    public ResponseEntity getAllPaymentSchemes(){
        List<PaymentSchemeDto> paymentSchemes = paymentSchemeService.getAllPaymentSchemes();
        return new ResponseEntity(
                new CommonResponseDTO(true, "Schemes loaded!", paymentSchemes), HttpStatus.OK
        );
    }
}
