package com.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class PaymentSchemeDto {

    private Integer schemeId;

    private String schemeName;

    private String schemeType;

    private List<PaymentPlanDto> paymentPlanEntityList;

    public PaymentSchemeDto(Integer schemeId, String schemeName, String schemeType, List<PaymentPlanDto> paymentPlanEntityList) {
        this.schemeId = schemeId;
        this.schemeName = schemeName;
        this.schemeType = schemeType;
        this.paymentPlanEntityList = paymentPlanEntityList;
    }



//    List<Map<String, Object>> paymentPlanEntityList;

}
