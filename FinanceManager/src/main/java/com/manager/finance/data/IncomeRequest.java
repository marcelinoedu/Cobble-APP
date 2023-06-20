package com.manager.finance.data;

import brave.internal.Nullable;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class IncomeRequest {

    private Long id;

    private String name;

    private BigDecimal amount;

    private String description;

    private String category;

    private LocalDate deadline;

    @Nullable
    private Boolean received;
    @Nullable
    private LocalDateTime received_at;

    public IncomeRequest(){

    }
    public IncomeRequest(Long id, String name,
                         BigDecimal amount,
                         String description,
                         String category,
                         LocalDate deadline,
                         Boolean received,
                         LocalDateTime received_at)
    {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.deadline = deadline;
        this.received = received;
        this.received_at = received_at;
    }


}
