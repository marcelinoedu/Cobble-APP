package com.manager.finance.controllers.IncomeControllers;

import com.manager.finance.data.IncomeRequest;
import com.manager.finance.models.IncomeModel;
import com.manager.finance.repositories.IncomeRepository;
import com.manager.finance.services.RequestService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/finance")
public class UpdateIncomeController {

    private final IncomeRepository incomeRepository;

    private final RequestService requestService;

    public UpdateIncomeController(IncomeRepository incomeRepository, RequestService requestService) {
        this.incomeRepository = incomeRepository;
        this.requestService = requestService;
    }


    @PutMapping("/income/{incomeId}")
    public ResponseEntity<?> updateIncome(@PathVariable Long incomeId, @RequestBody IncomeRequest incomeRequest) {
        Optional<IncomeModel> optionalIncome = incomeRepository.findById(incomeId);
        if (optionalIncome.isPresent()) {
            IncomeModel existingIncome = optionalIncome.get();

            if (requestService.areAllFieldsEmpty(incomeRequest)) {
                return ResponseEntity.badRequest().body("Nenhum dado da sua receita foi alterado.");
            }

            BeanUtils.copyProperties(incomeRequest, existingIncome, requestService.getNullPropertyNames(incomeRequest));

            existingIncome.setUpdated_at(LocalDateTime.now());

            incomeRepository.save(existingIncome);

            return ResponseEntity.ok("Os dados da sua receita foram atualizados");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar a receita informada.");
    }




//    @PutMapping("/income/{incomeId}")
//    public ResponseEntity<?> updateIncome(@PathVariable Long incomeId, @RequestBody IncomeRequest incomeRequest) {
//        Optional<IncomeModel> optionalIncome = incomeRepository.findById(incomeId);
//        if (optionalIncome.isPresent()) {
//            IncomeModel existingIncome = optionalIncome.get();
//
//            if (incomeRequest.getName() != null) {
//                existingIncome.setName(incomeRequest.getName());
//            }
//
//            if (incomeRequest.getAmount() != null) {
//                existingIncome.setAmount(incomeRequest.getAmount());
//            }
//
//            if (incomeRequest.getDeadline() != null) {
//                existingIncome.setDeadline(incomeRequest.getDeadline());
//            }
//
//            if (incomeRequest.getDescription() != null) {
//                existingIncome.setDescription(incomeRequest.getDescription());
//            }
//
//            if (incomeRequest.getReceived() != null) {
//                existingIncome.setReceived(incomeRequest.getReceived());
//            }
//
//            if (incomeRequest.getCategory() != null) {
//                existingIncome.setCategory(incomeRequest.getCategory());
//            }
//
//            existingIncome.setUpdated_at(LocalDateTime.now());
//            incomeRepository.save(existingIncome);
//
//            return ResponseEntity.ok("Os dados da sua receita foram atualizados");
//        }
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar a receita informada.");
//    }

}
