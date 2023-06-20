package com.manager.finance.data;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@Component
public class InstallmentRequest {

    private Long installment_id;

    private BigDecimal amount;

    private LocalDate deadline;

    private Boolean paid;

    private LocalDateTime paid_at;

    private Long expense_id;


    public InstallmentRequest(){

    }
    public InstallmentRequest(Long installment_id, BigDecimal amount, LocalDate deadline, Boolean paid, LocalDateTime paid_at, Long expense_id){
        this.installment_id = installment_id;
        this.amount = amount;
        this.deadline = deadline;
        this.paid = paid;
        this.paid_at = paid_at;
        this.expense_id =expense_id;
    }
}
