package com.example.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPaymentSchemeRequestDto {

    @NotNull
    private String schemeName;

    @NotNull
    private String schemeType;

//    private List<PaymentPlanEntity> paymentPlan;

    List<Map<String, Object>> paymentPlanEntityList;
}
