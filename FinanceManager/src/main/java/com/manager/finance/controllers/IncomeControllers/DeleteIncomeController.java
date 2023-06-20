package com.manager.finance.controllers.IncomeControllers;

import com.manager.finance.models.IncomeModel;
import com.manager.finance.repositories.IncomeRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/finance")
public class DeleteIncomeController {

    private final IncomeRepository incomeRepository;

    public DeleteIncomeController(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    @DeleteMapping("/income/{incomeId}")
    public ResponseEntity<?>deteleIncome(@PathVariable Long incomeId)
    {
        Optional<IncomeModel> optionalIncome = incomeRepository.findById(incomeId);
        if (optionalIncome.isPresent()){
            incomeRepository.deleteById(incomeId);
            return ResponseEntity.ok("receita removida.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível identificar a receita informada.");

    }
}
