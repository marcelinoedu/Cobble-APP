package com.manager.finance.controllers.DependencyControllers;

import com.manager.finance.models.DependencyModel;
import com.manager.finance.repositories.DependencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/finance")
public class DeleteDependencyController {

    @Autowired
    private final DependencyRepository dependenceRepository;

    public DeleteDependencyController(DependencyRepository dependencyRepository) {
        this.dependenceRepository = dependencyRepository;
    }

    @DeleteMapping("/dependency/{dependencyId}")
    public ResponseEntity<?>deleteDependency(@PathVariable Long dependencyId){
        Optional<DependencyModel> depencyOptional = dependenceRepository.findById(dependencyId);
        if (depencyOptional.isPresent()){
            dependenceRepository.deleteById(dependencyId);
            return ResponseEntity.ok("A dependência foi removida da sua depespesa.");
        }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("A dependência não foi encontrada para a despensa especificada.");

    }
}
