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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
public class InsertIncomeController {

    private final JwtService jwtService;

    private final RequestService requestService;

    private final IncomeRepository incomeRepository;

    public InsertIncomeController(JwtService jwtService, RequestService requestService, IncomeRepository incomeRepository) {
        this.jwtService = jwtService;
        this.requestService = requestService;
        this.incomeRepository = incomeRepository;
    }

    @PostMapping("/income")
    public ResponseEntity<?> createIncome(@RequestBody IncomeRequest incomeRequest,
                                          HttpServletRequest request)
    {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Long userId = jwtService.extractUserId(token);
        IncomeModel income = new IncomeModel();

        if (token.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado ou não encontrado em nosso sistema");
        }

        if (requestService.areAllFieldsEmpty(incomeRequest)) {
            return ResponseEntity.badRequest()
                    .body("Não foi possível adicionar sua receita, por favor preencha os campos corretamente!");
        }

        BeanUtils.copyProperties(incomeRequest, income, requestService.getNullPropertyNames(incomeRequest));
        income.setUserId(userId);
        incomeRepository.save(income);
        return ResponseEntity.ok("Sua receita foi salva!");

    }
}
