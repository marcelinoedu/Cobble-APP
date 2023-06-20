package com.manager.finance.controllers.IncomeControllers;

import com.manager.finance.data.IncomeRequest;
import com.manager.finance.models.IncomeModel;
import com.manager.finance.repositories.IncomeRepository;
import com.manager.finance.services.JwtService;
import com.manager.finance.services.RequestService;
import jakarta.servlet.http.HttpServletRequest;
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
public class GetIncomeController {


    private final JwtService jwtService;

    private final IncomeRepository incomeRepository;


    private final IncomeRequest incomeRequest;

    private final RequestService requestService;

    public GetIncomeController(JwtService jwtService, IncomeRepository incomeRepository, IncomeRequest incomeRequest, RequestService requestService) {
        this.jwtService = jwtService;
        this.incomeRepository = incomeRepository;
        this.incomeRequest = incomeRequest;
        this.requestService = requestService;
    }

    @GetMapping("/incomes")
    public ResponseEntity<?> getIncomes(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Long user_id = jwtService.extractUserId(token);

        List<IncomeModel> incomelist = incomeRepository.findIncomeByuserId(user_id);

        if(!incomelist.isEmpty())
        {
            return ResponseEntity.ok(incomelist);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O usuários informado não possui receitas cadastradas.");

    }

    @GetMapping("/income/{incomeId}")
    public ResponseEntity<?> getIncome(@PathVariable Long incomeId){
        Optional<IncomeModel> optionalIncome = incomeRepository.findById(incomeId);

        if (optionalIncome.isPresent())
        {
            IncomeModel income = optionalIncome.get();
            BeanUtils.copyProperties(income, incomeRequest);
            return ResponseEntity.ok(incomeRequest);

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar a receita informada.");
    }
}
