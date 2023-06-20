package com.manager.finance.services;

import com.manager.finance.data.ExpenseRequest;
import com.manager.finance.data.InstallmentRequest;
import com.manager.finance.models.ExpenseModel;
import com.manager.finance.models.InstallmentModel;
import com.manager.finance.repositories.ExpenseRepository;
import com.manager.finance.repositories.InstallmentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InstallmentService {

    private final InstallmentRepository installmentRepository;

    private final ExpenseRepository expenseRepository;

    public InstallmentService(InstallmentRepository installmentRepository, ExpenseRepository expenseRepository) {
        this.installmentRepository = installmentRepository;
        this.expenseRepository = expenseRepository;
    }

    public void updateInstallmentsAmount(ExpenseModel existingExpense, ExpenseRequest expenseRequest){
        BigDecimal requestAmount = expenseRequest.getAmount();
        BigDecimal expenseAmount = existingExpense.getAmount();
        int currentInstallments = existingExpense.getInstallments();
        if (!expenseAmount.equals(requestAmount)) {
            List<InstallmentModel> installments = existingExpense.getInstallment();
            for (InstallmentModel installment : installments) {
                BigDecimal updatedInstallmentValue = requestAmount.divide(BigDecimal.valueOf(currentInstallments), RoundingMode.HALF_UP);
                installment.setAmount(updatedInstallmentValue);
                existingExpense.setUpdatedAt(LocalDateTime.now());
            }
            installmentRepository.saveAll(installments);
        }
    }
    public String payInstallment(InstallmentModel existingInstallment, InstallmentRequest installmentRequest) {
        Boolean setInstallmentValue = installmentRequest.getPaid();
        Boolean installmentToPay = existingInstallment.getPaid();
        ExpenseModel expense = existingInstallment.getExpense();

        if (installmentToPay != setInstallmentValue) {
            existingInstallment.setPaid(installmentRequest.getPaid());
            existingInstallment.setPaidAt(LocalDateTime.now());
            installmentRepository.save(existingInstallment);

            int remainingInstallments = installmentRepository.findAllByPaidFalse().size();
            String concatenation = getConcatenationString(remainingInstallments);
            setExpenseStatus(expense, remainingInstallments);

            String statusMessage = (setInstallmentValue) ? "foi paga! " : "";
            return "Você acaba de modificar o status da parcela, " + statusMessage + concatenation;
        }

        return "Nenhuma parcela foi modificada!";
    }

    public void payAllInstallments(Long expenseId) {
        Optional<ExpenseModel> optionalExpense = expenseRepository.findById(expenseId);
        if (optionalExpense.isPresent()) {
            ExpenseModel expense = optionalExpense.get();
            List<InstallmentModel> installments = installmentRepository.findByExpenseId(expenseId);
            for (InstallmentModel installment : installments) {
                if (!installment.getPaid()) {
                    installment.setPaid(true);
                    installment.setPaidAt(LocalDateTime.now());
                }
            }
            expense.setUpdatedAt(LocalDateTime.now());
            expense.setPaid(true);
            installmentRepository.saveAll(installments);
            expenseRepository.save(expense);
        }
    }
    private String getConcatenationString(int remainingInstallments) {
        if (remainingInstallments > 1) {
            return "restam " + remainingInstallments + " parcelas";
        } else if (remainingInstallments == 1) {
            return "resta apenas 1 parcela";
        } else {
            return "Agora todas as parcelas foram pagas!";
        }
    }
    public void setExpenseStatus(ExpenseModel expense, Integer remainingInstallments) {
        expense.setUpdatedAt(LocalDateTime.now());
        expense.setPaid(remainingInstallments < 1);
        expenseRepository.save(expense);
    }

}
