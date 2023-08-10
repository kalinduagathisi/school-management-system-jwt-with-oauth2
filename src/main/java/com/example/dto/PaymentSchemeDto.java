package com.example.dto;

import com.example.entity.PaymentPlanEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

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
