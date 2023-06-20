package com.manager.finance.repositories;

import com.manager.finance.models.DependencyModel;
import com.manager.finance.models.InstallmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstallmentRepository extends JpaRepository<InstallmentModel, Long> {
    List<InstallmentModel> findByExpenseId(Long expenseId);

    @Query("SELECT i FROM InstallmentModel i WHERE i.paid = false")
    List<InstallmentModel> findAllByPaidFalse();
    default void deleteInstallmentsByExpenseId(Long expenseId) {
        List<InstallmentModel> installments = findByExpenseId(expenseId);
        deleteAll(installments);
    }
}
