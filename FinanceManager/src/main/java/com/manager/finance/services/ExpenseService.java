package com.manager.finance.services;

import com.manager.finance.data.ExpenseRequest;
import com.manager.finance.models.DependencyModel;
import com.manager.finance.models.ExpenseModel;
import com.manager.finance.models.InstallmentModel;
import com.manager.finance.repositories.DependencyInstallmentRepository;
import com.manager.finance.repositories.DependencyRepository;
import com.manager.finance.repositories.ExpenseRepository;
import com.manager.finance.repositories.InstallmentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class ExpenseService{

    private final RequestService requestService;
    private final InstallmentRepository installmentRepository;
    private final ExpenseRepository expenseRepository;
    private final InstallmentService installmentService;
    private final DependencyInstallmentRepository dependencyInstallmentRepository;
    private final DependencyRepository dependencyRepository;

    public ExpenseService(RequestService requestService, InstallmentRepository installmentRepository,
                          ExpenseRepository expenseRepository, InstallmentService installmentService, DependencyInstallmentRepository dependencyInstallmentRepository, DependencyRepository dependencyRepository) {
        this.requestService = requestService;
        this.installmentRepository = installmentRepository;
        this.expenseRepository = expenseRepository;
        this.installmentService = installmentService;
        this.dependencyInstallmentRepository = dependencyInstallmentRepository;
        this.dependencyRepository = dependencyRepository;
    }

    //-------------------------------------- Insert Expense section -------------------------------//
    public void insertExpense(ExpenseRequest expenseRequest, Long userId) {
        ExpenseModel expense = new ExpenseModel();
        BeanUtils.copyProperties(expenseRequest, expense, requestService.getNullPropertyNames(expenseRequest));
        expense.setUserId(userId);
        expenseRepository.save(expense);
        insertInstallment(expenseRequest, expense);
    }
    public void insertInstallment(ExpenseRequest expenseRequest, ExpenseModel expense) {
        if (expenseRequest.getInstallments() == 1) {
            InstallmentModel installment = new InstallmentModel();
            installment.setAmount(expenseRequest.getAmount());
            installment.setDeadline(expenseRequest.getDeadline());
            installment.setExpense(expense);
            installmentRepository.save(installment);
        } else if (expenseRequest.getInstallments() >= 2) {
            BigDecimal numberOfInstallments = new BigDecimal(expenseRequest.getInstallments());
            BigDecimal expenseAmount = expenseRequest.getAmount();
            BigDecimal installmentValue = expenseAmount.divide(numberOfInstallments, RoundingMode.HALF_UP);
            LocalDate installmentDate = expenseRequest.getDeadline();

            for (int i = 1; i <= expenseRequest.getInstallments(); i++) {
                InstallmentModel installment = new InstallmentModel();
                installment.setAmount(installmentValue);
                installment.setDeadline(installmentDate);
                installment.setExpense(expense);
                installmentRepository.save(installment);
                installmentDate = installmentDate.plusMonths(1);
            }
        }
    }

    //-------------------------------------- Update expense section -------------------------------//
    public void updateExpense(ExpenseRequest expenseRequest, ExpenseModel existingExpense) {
        if (!expenseRequest.getAmount().equals(existingExpense.getAmount())) {
            installmentService.updateInstallmentsAmount(existingExpense, expenseRequest);
            BigDecimal newExpenseAmount = expenseRequest.getAmount();
            existingExpense.setAmount(newExpenseAmount);
            expenseRepository.save(existingExpense);
        }
        BeanUtils.copyProperties(expenseRequest, existingExpense, requestService.getNullPropertyNames(expenseRequest));
        existingExpense.setUpdatedAt(LocalDateTime.now());
        expenseRepository.save(existingExpense);
    }

    //-------------------------------------- Update installment Quantity section -------------------------------//
    public void updateInstallmentsQuantity(ExpenseModel existingExpense, ExpenseRequest expenseRequest) {
        BigDecimal expenseAmount = existingExpense.getAmount();
        int currentInstallments = existingExpense.getInstallments();
        if (expenseRequest.getInstallments() != null){
            int newInstallments = expenseRequest.getInstallments();
            BigDecimal newInstallmentValue = expenseAmount.divide(BigDecimal.valueOf(newInstallments), RoundingMode.HALF_UP);
            if (currentInstallments != newInstallments) {
                if (newInstallments > currentInstallments) {
                    addNewInstallments(existingExpense, newInstallments, newInstallmentValue);
                } else {
                    removeInstallments(existingExpense, newInstallments);
                }
                updateRemainingInstallmentsAmount(existingExpense, newInstallmentValue);
                existingExpense.setUpdatedAt(LocalDateTime.now());
                existingExpense.setInstallments(newInstallments);
                expenseRepository.save(existingExpense);
            }
        }
    }

    private void addNewInstallments(ExpenseModel existingExpense, int newInstallments, BigDecimal newInstallmentValue) {
        LocalDate installmentDate = existingExpense.getDeadline();
        List<InstallmentModel> newInstallmentsList = new ArrayList<>();
        for (int i = existingExpense.getInstallments() + 1; i <= newInstallments; i++) {
            InstallmentModel installment = new InstallmentModel();
            installment.setAmount(newInstallmentValue);
            installment.setDeadline(installmentDate);
            installment.setExpense(existingExpense);
            newInstallmentsList.add(installment);
            installmentDate = installmentDate.plusMonths(1);
        }
        installmentRepository.saveAll(newInstallmentsList);
    }
    private void removeInstallments(ExpenseModel existingExpense, int newInstallments) {
        List<InstallmentModel> installmentsToRemove = installmentRepository.findByExpenseId(existingExpense.getId()).subList(newInstallments, existingExpense.getInstallments());
        installmentRepository.deleteAll(installmentsToRemove);
    }
    private void updateRemainingInstallmentsAmount(ExpenseModel existingExpense, BigDecimal newInstallmentValue) {
        List<InstallmentModel> remainingInstallments = installmentRepository.findByExpenseId(existingExpense.getId());
        for (InstallmentModel installment : remainingInstallments) {
            installment.setAmount(newInstallmentValue);
        }
        installmentRepository.saveAll(remainingInstallments);
    }

    //----------------------------------------------//
    public void deleteExpense(Long expenseId) {
        List<DependencyModel> dependencies = dependencyRepository.findByExpenseId(expenseId);

        for (DependencyModel dependency : dependencies) {
            dependencyInstallmentRepository.deleteInstallmentsByDependencyId(dependency.getId());
            dependencyRepository.deleteById(dependency.getId());
        }

        installmentRepository.deleteInstallmentsByExpenseId(expenseId);
        expenseRepository.deleteById(expenseId);
    }



}
