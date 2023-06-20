package com.manager.finance.data;

import brave.internal.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@Component
public class DependencyRequest {

    private Long id;

    private String dependent_name;

    private BigDecimal amount;

    private LocalDate date;

    private Integer installments;

    private LocalDate deadline_to_receive;

    @Nullable
    private byte[] receipt;

    public DependencyRequest()
    {}

    public DependencyRequest(Long id,
                             String dependent_name,
                             BigDecimal amount,
                             LocalDate date,
                             Integer installments,
                             LocalDate deadline_to_receive,
                             byte[] receipt)
    {
        this.id = id;
        this.dependent_name = dependent_name;
        this.amount = amount;
        this.date = date;
        this.installments = installments;
        this.deadline_to_receive = deadline_to_receive;
        this.receipt = receipt;
    }

}
