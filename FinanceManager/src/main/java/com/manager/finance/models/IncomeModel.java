package com.manager.finance.models;

import brave.internal.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "incomes")
public class IncomeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private Long userId;

    private String name;

    private BigDecimal amount;

    private String description;

    private String category;

    private LocalDate deadline;

    private Boolean received;

    private LocalDateTime received_at;

    private LocalDateTime updated_at;

    @CreationTimestamp
    private LocalDateTime created_at;

}
