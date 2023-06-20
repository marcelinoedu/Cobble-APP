package com.manager.finance.controllers.ExpenseControllers;

import com.manager.finance.data.ExpenseRequest;
import com.manager.finance.data.InstallmentRequest;
import com.manager.finance.models.ExpenseModel;
import com.manager.finance.models.InstallmentModel;
import com.manager.finance.repositories.ExpenseRepository;
import com.manager.finance.repositories.InstallmentRepository;
import com.manager.finance.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/finance")
public class GetExpenseController {
    private final JwtService jwtService;
    private final ExpenseRepository expenseRepository;
    private final ExpenseRequest expenseRequest;
    public GetExpenseController(JwtService jwtService,
                                ExpenseRepository expenseRepository,
                                ExpenseRequest expenseRequest) {
        this.jwtService = jwtService;
        this.expenseRepository = expenseRepository;
        this.expenseRequest = expenseRequest;
    }
    @GetMapping("/expenses")
    public ResponseEntity<?>getExpenses(HttpServletRequest request)
    {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Long user_id = jwtService.extractUserId(token);

        List<ExpenseModel> expenseList = expenseRepository.findExpenseByuserId(user_id);
        if (!expenseList.isEmpty()){
            return ResponseEntity.ok(expenseList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("O usuário especificado não possui despesas.");
    }
    @GetMapping("/expense/{expenseId}")
    public ResponseEntity<?>getExpense(@PathVariable Long expenseId) {
        Optional<ExpenseModel> expenseOptional = expenseRepository.findById(expenseId);
        if (expenseOptional.isPresent()) {
            ExpenseModel expense = expenseOptional.get();
            BeanUtils.copyProperties(expense, expenseRequest);
            return ResponseEntity.ok(expenseRequest);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Não foi possível encontrar informações para a despesa informada.");
    }

}