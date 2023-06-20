package com.manager.authserver.service;

import com.manager.authserver.repositories.TokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService{
    private final TokenRepository tokenRepository;


    public AuthService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public ResponseEntity<?> validateToken(String token) {

            if (token != null && token.startsWith("Bearer ")) {
                try {
                    String jwtToken = token.substring(7);
                    var isTokenValid = tokenRepository.findByToken(jwtToken).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
                    if(isTokenValid){
                        return ResponseEntity.ok().build();
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expirado ou inválido");
                    }
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro ao validar token: " + e.getMessage());
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }
}
