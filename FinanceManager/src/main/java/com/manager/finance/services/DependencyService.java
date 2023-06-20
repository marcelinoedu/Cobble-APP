package com.manager.finance.services;

import com.manager.finance.data.DependencyRequest;
import com.manager.finance.data.ExpenseRequest;
import com.manager.finance.data.InstallmentRequest;
import com.manager.finance.models.DependencyInstallmentModel;
import com.manager.finance.models.DependencyModel;
import com.manager.finance.models.ExpenseModel;
import com.manager.finance.models.InstallmentModel;
import com.manager.finance.repositories.DependencyInstallmentRepository;
import com.manager.finance.repositories.DependencyRepository;
import org.hibernate.Interceptor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DependencyService {

    private final RequestService requestService;
    private final DependencyInstallmentRepository dependencyInstallmentRepository;
    private final DependencyRepository dependencyRepository;

    public DependencyService(RequestService requestService, DependencyInstallmentRepository dependencyInstallmentRepository, DependencyRepository dependencyRepository) {
        this.requestService = requestService;
        this.dependencyInstallmentRepository = dependencyInstallmentRepository;
        this.dependencyRepository = dependencyRepository;
    }
    public void insertDependency(DependencyRequest dependencyRequest, ExpenseModel existingExpense){
        DependencyModel dependency =  new DependencyModel();
        BeanUtils.copyProperties(dependencyRequest, dependency, requestService.getNullPropertyNames(dependencyRequest));
        dependency.setExpense(existingExpense);
        dependencyRepository.save(dependency);
        if (dependencyRequest.getInstallments() != null){
            createDependencyInstallments(dependencyRequest, dependency);
        }
    }

    public void createDependencyInstallments(DependencyRequest dependencyRequest,
                                             DependencyModel dependency){
        if (dependencyRequest.getInstallments() == 1) {
            DependencyInstallmentModel installment = new DependencyInstallmentModel();
            installment.setAmount(dependencyRequest.getAmount());
            installment.setDeadline(dependencyRequest.getDeadline_to_receive());
            installment.setDependency(dependency);
            dependencyInstallmentRepository.save(installment);
        } else if (dependencyRequest.getInstallments() >= 2) {
            BigDecimal numberOfInstallments = new BigDecimal(dependencyRequest.getInstallments());
            BigDecimal dependencyAmount = dependencyRequest.getAmount();
            BigDecimal installmentValue = dependencyAmount.divide(numberOfInstallments, RoundingMode.HALF_UP);
            LocalDate installmentDate = dependencyRequest.getDeadline_to_receive();

            for (int i = 1; i <= dependencyRequest.getInstallments(); i++) {
                DependencyInstallmentModel installment = new DependencyInstallmentModel();
                installment.setAmount(installmentValue);
                installment.setDeadline(installmentDate);
                installment.setDependency(dependency);
                dependencyInstallmentRepository.save(installment);
                installmentDate = installmentDate.plusMonths(1);
            }
        }
    }

    public void updateDependency(DependencyRequest dependencyRequest,
                                 DependencyModel existingDependency) {
        if (dependencyRequest.getAmount() != null){
            if (!dependencyRequest.getAmount().equals(existingDependency.getAmount())) {
                updateDependencyInstallmentsAmount(existingDependency, dependencyRequest);
                BigDecimal newExpenseAmount = dependencyRequest.getAmount();
                existingDependency.setAmount(newExpenseAmount);
                dependencyRepository.save(existingDependency);
            }
        }
        if (dependencyRequest.getInstallments() != null){
            if (!dependencyRequest.getInstallments().equals(existingDependency.getInstallments())){
                updateDependencyInstallmentsQuantity(existingDependency, dependencyRequest);
            }
        }

        BeanUtils.copyProperties(dependencyRequest, existingDependency, requestService.getNullPropertyNames(dependencyRequest));
        dependencyRepository.save(existingDependency);
    }

    public void updateDependencyInstallmentsQuantity(DependencyModel existingDependency,
                                                     DependencyRequest dependencyRequest){
        BigDecimal dependencyAmount = existingDependency.getAmount();
        int currentInstallments = existingDependency.getInstallments();
        if (dependencyRequest.getInstallments() != null){
            int newInstallments = dependencyRequest.getInstallments();
            BigDecimal newInstallmentsValue = dependencyAmount.divide(BigDecimal.valueOf(newInstallments), RoundingMode.HALF_UP);
            if (currentInstallments != newInstallments){
                if (newInstallments > currentInstallments){
                    addNewInstallments(existingDependency, newInstallments, newInstallmentsValue);
                } else {
                    removeInstallments(existingDependency, newInstallments);
                }
                updateRemainingInstallmentsAmount(existingDependency, newInstallmentsValue);
                existingDependency.setInstallments(newInstallments);
                dependencyRepository.save(existingDependency);
            }
        }
    }
    private void addNewInstallments(DependencyModel existingDependency, int newInstallments, BigDecimal newInstallmentValue){
        LocalDate dependencyInstallmentDate = existingDependency.getDeadline_to_receive();
        List<DependencyInstallmentModel> newInstallmentsList = new ArrayList<>();
        for (int i = existingDependency.getInstallments() + 1; i <= newInstallments; i++){
            DependencyInstallmentModel installment =new DependencyInstallmentModel();
            installment.setAmount(newInstallmentValue);
            installment.setDeadline(dependencyInstallmentDate);
            installment.setDependency(existingDependency);
            newInstallmentsList.add(installment);
            dependencyInstallmentDate = dependencyInstallmentDate.plusMonths(1);
        }
        dependencyInstallmentRepository.saveAll(newInstallmentsList);
    }
    private void removeInstallments(DependencyModel existingDependency, int newInstallments){
        List<DependencyInstallmentModel> installmentsToRemove = dependencyInstallmentRepository.findByDependencyId(existingDependency.getId()).subList(newInstallments, existingDependency.getInstallments());
        dependencyInstallmentRepository.deleteAll(installmentsToRemove);
    }
    private void updateRemainingInstallmentsAmount(DependencyModel existingDependency, BigDecimal newInstallmentValue) {
        List<DependencyInstallmentModel> remainingInstallments = dependencyInstallmentRepository.findByDependencyId(existingDependency.getId());
        for (DependencyInstallmentModel installment : remainingInstallments) {
            installment.setAmount(newInstallmentValue);
        }
        dependencyInstallmentRepository.saveAll(remainingInstallments);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------//
    public void updateDependencyInstallmentsAmount(DependencyModel existingDependency,
                                         DependencyRequest dependencyRequest){
            BigDecimal requestAmount = dependencyRequest.getAmount();
            BigDecimal dependencyAmount = existingDependency.getAmount();
            int currentInstallments = existingDependency.getInstallments();
            if (!dependencyAmount.equals(requestAmount)){
                List<DependencyInstallmentModel> installments = existingDependency.getInstallment();
                for (DependencyInstallmentModel installment : installments){
                    BigDecimal updatedInstallmentsValue = requestAmount.divide(BigDecimal.valueOf(currentInstallments), RoundingMode.HALF_UP);
                    installment.setAmount(updatedInstallmentsValue);
                }
                dependencyInstallmentRepository.saveAll(installments);
            }

    }
    public void payDependencyInstallment(DependencyInstallmentModel existingInstallment,
                                           InstallmentRequest installmentRequest){
        Boolean setInstallmentValue = installmentRequest.getPaid();
        Boolean installmentToPay = existingInstallment.getPaid();
        DependencyModel dependency = existingInstallment.getDependency();

        if (installmentToPay != setInstallmentValue){
            existingInstallment.setPaid(installmentRequest.getPaid());
            existingInstallment.setPaidAt(LocalDateTime.now());
            dependencyInstallmentRepository.save(existingInstallment);
        }
    }

    public void payAllDependencyInstallments(Long dependencyId){
        Optional<DependencyModel> optionalDependency = dependencyRepository.findById(dependencyId);
        if (optionalDependency.isPresent()){
            DependencyModel dependency = optionalDependency.get();
            List<DependencyInstallmentModel> installments = dependencyInstallmentRepository.findByDependencyId(dependencyId);
            for (DependencyInstallmentModel installment : installments){
                if (!installment.getPaid()){
                    installment.setPaid(true);
                    installment.setPaidAt(LocalDateTime.now());
                }
            }
            dependencyInstallmentRepository.saveAll(installments);
        }
    }



}
