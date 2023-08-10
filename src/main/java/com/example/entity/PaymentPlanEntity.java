package com.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "payment_plan")
public class PaymentPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_plan_id")
    private Integer paymentPlanId;
    private String feeType;
    private BigDecimal amount;

    @JsonIgnore
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "schemeId")
//    @JsonIdentityReference(alwaysAsId = true)  // returns only the id of the required entity
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_scheme_id")
    private PaymentSchemeEntity paymentSchemeEntity;


    // round off decimal value to two places
    @PrePersist
    public void roundAmountBeforePersist() {
        // Round off the amount to two decimal places
        if (amount != null) {
            amount = amount.setScale(2, RoundingMode.HALF_UP);
        }
    }

}
