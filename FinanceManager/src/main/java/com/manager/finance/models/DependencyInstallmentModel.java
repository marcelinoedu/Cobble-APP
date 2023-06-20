package com.manager.finance.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dependency_installments")
public class DependencyInstallmentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long installment_id;

    private BigDecimal amount;

    private LocalDate deadline;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean paid = false;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @ManyToOne
    @JoinColumn(name="dependency_id")
    @JsonIgnore
    private DependencyModel dependency;
}
