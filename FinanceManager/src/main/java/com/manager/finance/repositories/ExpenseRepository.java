package com.manager.finance.repositories;

import com.manager.finance.models.ExpenseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<ExpenseModel, Long> {
    Optional<ExpenseModel>findById(Long id);

    List<ExpenseModel>findExpenseByuserId(Long user_id);
}
