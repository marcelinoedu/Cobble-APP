package com.manager.user.repositories;

import com.manager.user.models.Token.TokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenModel, Long> {

    @Query("SELECT t FROM TokenModel t " +
            "INNER JOIN t.user u " +
            "WHERE u.id = :userId AND t.expired = false AND t.revoked = false")
    List<TokenModel> findAllValidTokensByUser(@Param("userId") Long userId);

    Optional<TokenModel> findByToken(String jwtToken);


}
