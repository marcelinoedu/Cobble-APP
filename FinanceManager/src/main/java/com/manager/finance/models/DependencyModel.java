package com.manager.finance.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dependencies")
public class DependencyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dependent_name;

    private BigDecimal amount;

    private LocalDate date;

    private Integer installments;

    private LocalDate deadline_to_receive;

    @Column(name = "receipt", columnDefinition="mediumblob", nullable = true)
    private byte[] receipt;

    @ManyToOne
    @JoinColumn(name="expense_id")
    @JsonIgnore
    private ExpenseModel expense;
    @OneToMany(mappedBy = "dependency")
    private List<DependencyInstallmentModel> installment;
}
