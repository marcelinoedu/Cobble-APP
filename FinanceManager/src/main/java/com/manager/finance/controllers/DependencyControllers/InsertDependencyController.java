package com.manager.finance.controllers.DependencyControllers;

import com.manager.finance.data.DependencyRequest;
import com.manager.finance.models.DependencyModel;
import com.manager.finance.models.ExpenseModel;
import com.manager.finance.repositories.DependencyRepository;
import com.manager.finance.repositories.ExpenseRepository;
import com.manager.finance.services.DependencyService;
import com.manager.finance.services.RequestService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/finance")
public class InsertDependencyController {

    private final ExpenseRepository expenseRepository;

    private final DependencyService dependencyService;
    private final DependencyRepository dependencyRepository;

    private final RequestService requestService;
    public InsertDependencyController(ExpenseRepository expenseRepository,
                                      DependencyService dependencyService, DependencyRepository dependencyRepository, RequestService requestService) {
        this.expenseRepository = expenseRepository;
        this.dependencyService = dependencyService;
        this.dependencyRepository = dependencyRepository;
        this.requestService = requestService;
    }


    @PostMapping("/dependency/{expenseId}")
    public ResponseEntity<?> addDependencyToExpense(@PathVariable Long expenseId,
                                                    @RequestBody DependencyRequest dependencyRequest) {
        Optional<ExpenseModel> expense = expenseRepository.findById(expenseId);
        if (expense.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Despesa não encontrada");
        }
        if (requestService.areAllFieldsEmpty(dependencyRequest)) {
            return ResponseEntity.badRequest()
                    .body("Não foi possível adicionar uma dependência, por favor preencha os campos corretamente!");
        }
        ExpenseModel existingExpense = expense.get();
        dependencyService.insertDependency(dependencyRequest, existingExpense);

        return ResponseEntity.ok("Dependência adicionada com sucesso!");
    }

}
