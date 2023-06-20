package com.manager.finance.controllers.ExpenseControllers;

import com.manager.finance.data.ExpenseRequest;
import com.manager.finance.models.ExpenseModel;
import com.manager.finance.repositories.ExpenseRepository;
import com.manager.finance.services.ExpenseService;
import com.manager.finance.services.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/finance")
public class UpdateExpenseController {
    private final ExpenseRepository expenseRepository;
    private final ExpenseService expenseService;
    private final RequestService requestService;

    public UpdateExpenseController(ExpenseRepository expenseRepository, ExpenseService expenseService, RequestService requestService) {
        this.expenseRepository = expenseRepository;
        this.expenseService = expenseService;
        this.requestService = requestService;
    }

    @PutMapping("/expense/{expenseId}")
    public ResponseEntity<?> updateExpense(@PathVariable Long expenseId,
                                           @RequestBody ExpenseRequest expenseRequest)
    {
        Optional<ExpenseModel> optionalExpense = expenseRepository.findById(expenseId);

        if (optionalExpense.isPresent()){
            ExpenseModel existingExpense = optionalExpense.get();
            if (requestService.areAllFieldsEmpty(expenseRequest)) {
                return ResponseEntity.badRequest().body("Nenhum dado da sua despesa foi alterado.");
            }
            expenseService.updateExpense(expenseRequest, existingExpense);
            return ResponseEntity.ok("Os dados da sua despesa foram atualizados");

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar a despesa informada.");

    }
    @PutMapping("/expense/installments/{expenseId}")
    public ResponseEntity<?> updateExpenseInstallments(@PathVariable Long expenseId,
                                                       @RequestBody ExpenseRequest expenseRequest){
        Optional<ExpenseModel> optionalExpense = expenseRepository.findById(expenseId);
        if (optionalExpense.isPresent()){
            if (requestService.areAllFieldsEmpty(expenseRequest)) {
                return ResponseEntity.badRequest().body("Nenhum dado da sua despesa foi alterado.");
            }
            ExpenseModel existingExpense = optionalExpense.get();
            expenseService.updateInstallmentsQuantity(existingExpense, expenseRequest);
            return ResponseEntity.ok("As parcelas desta despesa foram alteradas");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar a despesa informada.");

    }
}
