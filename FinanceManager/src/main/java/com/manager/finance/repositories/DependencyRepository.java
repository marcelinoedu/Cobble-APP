package com.manager.finance.repositories;

import com.manager.finance.models.DependencyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DependencyRepository extends JpaRepository<DependencyModel, Long> {

    Optional<DependencyModel> findById(Long dependencyId);

    List<DependencyModel> findByExpenseId(Long expenseId);

    void deleteById(Long dependencyId);
    default void deleteDependenciesByExpenseId(Long expenseId) {
        List<DependencyModel> dependencies = findByExpenseId(expenseId);
        deleteAll(dependencies);
    }
}
