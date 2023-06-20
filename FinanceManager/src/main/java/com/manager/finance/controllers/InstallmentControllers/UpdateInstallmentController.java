package com.manager.finance.controllers.InstallmentControllers;

import com.manager.finance.data.InstallmentRequest;
import com.manager.finance.models.ExpenseModel;
import com.manager.finance.models.InstallmentModel;
import com.manager.finance.repositories.ExpenseRepository;
import com.manager.finance.repositories.InstallmentRepository;
import com.manager.finance.services.ExpenseService;
import com.manager.finance.services.InstallmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/finance")
public class UpdateInstallmentController {

    private final InstallmentRepository installmentRepository;
    private final ExpenseRepository expenseRepository;
    private final InstallmentService installmentService;



    public UpdateInstallmentController(InstallmentRepository installmentRepository,
                                       ExpenseRepository expenseRepository,
                                       InstallmentService installmentService) {
        this.installmentRepository = installmentRepository;
        this.expenseRepository = expenseRepository;
        this.installmentService = installmentService;
    }

    @PutMapping("/installment/{installmentId}")
    public ResponseEntity<?> payInstallment(@PathVariable Long installmentId,
                                            @RequestBody InstallmentRequest installmentRequest){
        Optional<InstallmentModel> optionalInstallment = installmentRepository.findById(installmentId);
        if (optionalInstallment.isPresent()){
            InstallmentModel existingInstallment = optionalInstallment.get();
            String response = installmentService.payInstallment(existingInstallment, installmentRequest);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar a parcela informada.");
    }

    @PutMapping("/pay/installments/{expenseId}")
    public ResponseEntity<?> payAllInstallments(@PathVariable Long expenseId) {
        Optional<ExpenseModel> optionalExpense = expenseRepository.findById(expenseId);
        if (optionalExpense.isPresent()) {
            installmentService.payAllInstallments(expenseId);
            return ResponseEntity.ok("Todas as suas despesas foram pagas!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar a despesa especifica!");
    }


}
