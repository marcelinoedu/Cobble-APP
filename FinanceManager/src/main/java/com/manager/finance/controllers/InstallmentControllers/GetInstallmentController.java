package com.manager.finance.controllers.InstallmentControllers;

import com.manager.finance.data.InstallmentRequest;
import com.manager.finance.models.InstallmentModel;
import com.manager.finance.repositories.InstallmentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/finance")
public class GetInstallmentController {

    private final InstallmentRepository installmentRepository;

    private final InstallmentRequest installmentRequest;

    public GetInstallmentController(InstallmentRepository installmentRepository, InstallmentRequest installmentRequest1) {
        this.installmentRepository = installmentRepository;
        this.installmentRequest = installmentRequest1;
    }

    @GetMapping("/installments/{expenseId}")
    public ResponseEntity<?> getInstallments(@PathVariable Long expenseId){
        List<InstallmentModel> installments = installmentRepository.findByExpenseId(expenseId);

        if (!installments.isEmpty()){
            return ResponseEntity.ok(installments);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("As parcelas não foram encontradas para a despensa especificada.");
    }

    @GetMapping("/installment/{installmentId}")
    public ResponseEntity<?>getInstallment(@PathVariable Long installmentId){
        Optional<InstallmentModel> optionalInstallment = installmentRepository.findById(installmentId);

        if (optionalInstallment.isPresent()){
            InstallmentModel installment = optionalInstallment.get();
            BeanUtils.copyProperties(installment, installmentRequest);
            return ResponseEntity.ok(installmentRequest);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("A parcela não foi encontrada para a despensa especificada.");
    }
}
