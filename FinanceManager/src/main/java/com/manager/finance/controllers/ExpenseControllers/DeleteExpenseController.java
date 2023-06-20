package com.manager.finance.controllers.ExpenseControllers;

import com.manager.finance.models.ExpenseModel;
import com.manager.finance.repositories.DependencyInstallmentRepository;
import com.manager.finance.repositories.ExpenseRepository;
import com.manager.finance.services.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/finance")
public class DeleteExpenseController {

    private final ExpenseRepository expenseRepository;

    private final ExpenseService expenseService;
    private final DependencyInstallmentRepository dependencyInstallmentRepository;


    public DeleteExpenseController(ExpenseRepository expenseRepository, ExpenseService expenseService, DependencyInstallmentRepository dependencyInstallmentRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseService = expenseService;
        this.dependencyInstallmentRepository = dependencyInstallmentRepository;
    }


    @DeleteMapping("/expense/{expenseId}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long expenseId)
    {
        Optional<ExpenseModel>optionalExpense = expenseRepository.findById(expenseId);

        if (optionalExpense.isPresent()){
            expenseService.deleteExpense(expenseId);
            return ResponseEntity.ok("Despesa removida com sucesso");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Despesa não encontrada.");
    }



}
