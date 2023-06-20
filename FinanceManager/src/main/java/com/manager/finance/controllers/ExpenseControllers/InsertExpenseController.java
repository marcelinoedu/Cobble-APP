package com.manager.finance.controllers.ExpenseControllers;

import com.manager.finance.data.ExpenseRequest;
import com.manager.finance.models.ExpenseModel;
import com.manager.finance.repositories.ExpenseRepository;
import com.manager.finance.services.ExpenseService;
import com.manager.finance.services.JwtService;
import com.manager.finance.services.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
public class InsertExpenseController {

    private final JwtService jwtService;
    private final RequestService requestService;
    private final ExpenseService expenseService;

    private final ExpenseRepository expenseRepository;

    public InsertExpenseController(JwtService jwtService,
                                   RequestService requestService,
                                   ExpenseService expenseService,
                                   ExpenseRepository expenseRepository) {
        this.jwtService = jwtService;
        this.requestService = requestService;
        this.expenseService = expenseService;
        this.expenseRepository = expenseRepository;
    }
    @PostMapping("/expense")
    public ResponseEntity<?>createExpense(@RequestBody ExpenseRequest expenseRequest,
                                          HttpServletRequest request)
    {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Long userId = jwtService.extractUserId(token);

        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado ou não encontrado em nosso sistema.");
        }
        if (requestService.areAllFieldsEmpty(expenseRequest)) {
            return ResponseEntity.badRequest()
                    .body("Não foi possível adicionar a uma nova despensa, por favor preencha os campos corretamente!");
        }
        expenseService.insertExpense(expenseRequest, userId);
        return ResponseEntity.ok("Sua despesa foi salva.");
    }
}
