package com.manager.user.services.token;

import com.manager.user.models.Token.TokenModel;
import com.manager.user.models.Token.TokenType;
import com.manager.user.models.User.UserModel;
import com.manager.user.repositories.TokenRepository;
import com.manager.user.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    public TokenService(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public void revokeAllUserTokens(UserModel userModel)
    {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(userModel.getId());
        if(validUserTokens.isEmpty()){
            return;
        }
            validUserTokens.forEach(t -> {
                t.setRevoked(true);
                t.setExpired(true);
            });

        tokenRepository.saveAll(validUserTokens);

    }

    public void revokeAllUserTokensById(Long userId) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(userId);
        if(validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    public TokenModel buildToken(String jwtToken, UserModel user){
        return TokenModel.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
    }
}
