package com.facade.userfacade.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final RouterValidator validator;
    private final RestTemplate restTemplate;

    public AuthFilter(RouterValidator validator, RestTemplate restTemplate) {
        this.validator = validator;
        this.restTemplate = restTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (validator.isSecured(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else {
            String token = authHeader.substring(7);
            try {
                restTemplate.getForObject("http://localhost:8889/auth/validate-token?token=Bearer " + token, String.class);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }
        filterChain.doFilter(request, response);
    }
}

