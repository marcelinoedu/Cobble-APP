package com.manager.finance.controllers.DependencyControllers;

import com.manager.finance.data.DependencyRequest;
import com.manager.finance.models.DependencyModel;
import com.manager.finance.repositories.DependencyRepository;
import com.manager.finance.services.JwtService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/finance")
public class GetDependencyController {

    private final DependencyRepository dependencyRepository;

    private final JwtService jwtService;

    private final DependencyRequest dependencyRequest;


    public GetDependencyController(DependencyRepository dependencyRepository, JwtService jwtService, DependencyRequest dependencyRequest) {
        this.dependencyRepository = dependencyRepository;
        this.jwtService = jwtService;
        this.dependencyRequest = dependencyRequest;
    }

    @GetMapping("/dependencies/{expenseId}")
    public ResponseEntity<?> getDependencies(@PathVariable Long expenseId) {

        List<DependencyModel> dependencies = dependencyRepository.findByExpenseId(expenseId);

        if (!dependencies.isEmpty()) {
            return ResponseEntity.ok(dependencies);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("As dependências não foram encontradas para a despensa especificada.");
    }
    @GetMapping("/dependency/{dependencyId}")
    public ResponseEntity<?>getDependency(@PathVariable Long dependencyId){

        Optional<DependencyModel> optionalDependency = dependencyRepository.findById(dependencyId);

        if (optionalDependency.isPresent()){
                DependencyModel dependency = optionalDependency.get();
                BeanUtils.copyProperties(dependency, dependencyRequest);
                return ResponseEntity.ok(dependencyRequest);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("A dependência não foi encontrada para a despensa especificada.");
    }



}
