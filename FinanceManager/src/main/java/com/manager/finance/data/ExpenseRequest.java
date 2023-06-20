package com.manager.finance.data;

import brave.internal.Nullable;
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
public class ExpenseRequest {

    private Long id;

    private Long userId;

    private String name;

    private BigDecimal amount;

    private String description;

    private String category;

    private LocalDate deadline;

    @Nullable
    private Boolean paid;

    @Nullable
    private Integer installments;

    public ExpenseRequest() {
    }

    public ExpenseRequest(Long id,
                          Long userId,
                          String name,
                          BigDecimal amount,
                          String description,
                          String category,
                          LocalDate deadline,
                          Boolean paid,
                          Integer installments) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.deadline = deadline;
        this.paid = paid;
        this.installments = installments;
    }

}
