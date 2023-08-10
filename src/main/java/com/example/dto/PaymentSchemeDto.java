package com.example.dto;

import com.example.entity.PaymentPlanEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSchemeDto {

    private Integer schemeId;

    private String schemeName;

    private String schemeType;

    private List<PaymentPlanEntity> paymentPlanEntity;
}
