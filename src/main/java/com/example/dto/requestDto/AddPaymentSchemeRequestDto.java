package com.example.dto.requestDto;

import com.example.entity.PaymentPlanEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddPaymentSchemeRequestDto {

    private String schemeName;

    private String schemeType;

//    private List<PaymentPlanEntity> paymentPlan;

    List<Map<String, Object>> paymentPlanEntityList;
}
