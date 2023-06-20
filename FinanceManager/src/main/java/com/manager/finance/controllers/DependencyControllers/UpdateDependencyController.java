package com.manager.finance.controllers.DependencyControllers;

import com.manager.finance.data.DependencyRequest;
import com.manager.finance.models.DependencyModel;
import com.manager.finance.repositories.DependencyRepository;
import com.manager.finance.services.DependencyService;
import com.manager.finance.services.RequestService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/finance")
public class UpdateDependencyController {

    private final DependencyRepository dependencyRepository;

    private final RequestService requestService;

    private final DependencyService dependencyService;

    public UpdateDependencyController(DependencyRepository dependencyRepository, RequestService requestService, DependencyService dependencyService) {
        this.dependencyRepository = dependencyRepository;
        this.requestService = requestService;
        this.dependencyService = dependencyService;
    }

    @PutMapping("/dependency/{dependencyId}")
    public ResponseEntity<?> updateDependency(@PathVariable Long dependencyId,
                                              @RequestBody DependencyRequest dependencyRequest)
    {
        Optional<DependencyModel> optionalDependency = dependencyRepository.findById(dependencyId);
        if (optionalDependency.isPresent()){
            DependencyModel existingDependency = optionalDependency.get();
            if (requestService.areAllFieldsEmpty(dependencyRequest)){
                return ResponseEntity.badRequest()
                        .body("Nenhum dado da sua dependência foi alterado.");
            }
            dependencyService.updateDependency(dependencyRequest, existingDependency);
            return ResponseEntity.ok("Os dados da sua dependência foram atualizados.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Não foi possível encontrar a dependência que deseja editar.");
    }
}
