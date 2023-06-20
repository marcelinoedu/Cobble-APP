package com.manager.authserver.repositories;

import com.manager.authserver.models.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByCpf(String cpf);

    Optional<UserModel> findById(Long id);

}
