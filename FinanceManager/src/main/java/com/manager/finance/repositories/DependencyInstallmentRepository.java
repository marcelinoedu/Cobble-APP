package com.manager.finance.repositories;

import com.manager.finance.models.DependencyInstallmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface DependencyInstallmentRepository extends JpaRepository<DependencyInstallmentModel, Long> {
    List<DependencyInstallmentModel> findByDependencyId(Long dependencyId);

    @Query("SELECT i FROM DependencyInstallmentModel i WHERE i.paid = false")
    List<DependencyInstallmentModel> findAllByPaidFalse();
    default void deleteInstallmentsByDependencyId(Long dependencyId) {
        List<DependencyInstallmentModel> dependencyInstallments = findByDependencyId(dependencyId);
        deleteAll(dependencyInstallments);
    }
}
