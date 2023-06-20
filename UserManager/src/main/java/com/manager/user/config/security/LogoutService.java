package com.manager.user.config.security;

import com.manager.user.repositories.TokenRepository;
import com.manager.user.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    private final JwtService jwtService;

    private final UserService userService;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication)
    {
        final String authHeader = request.getHeader("Authorization");
        final String token;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        token = authHeader.substring(7);
        Long userId = jwtService.extractUserId(token);
        userService.updateLastLogout(userId);
        var storedToken = tokenRepository.findByToken(token).orElse(null);
        if(storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }

    }

}
