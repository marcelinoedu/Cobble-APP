package com.manager.finance.repositories;

import com.manager.finance.models.IncomeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository <IncomeModel, Long>{
    Optional<IncomeModel> findById(Long Id);

    List<IncomeModel> findIncomeByuserId(Long user_id);
}
