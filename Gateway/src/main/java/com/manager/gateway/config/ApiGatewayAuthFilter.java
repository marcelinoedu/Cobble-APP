package com.manager.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ApiGatewayAuthFilter extends AbstractGatewayFilterFactory<ApiGatewayAuthFilter.Config> {

    public ApiGatewayAuthFilter(){
        super(ApiGatewayAuthFilter.Config.class);
    }

    @Autowired
    private RouterValidator validator;

    @Autowired
    private RestTemplate template;

    @Override
    public GatewayFilter apply(ApiGatewayAuthFilter.Config config){
        return ((exchange, chain) -> {
            if(validator.isSecured.test(exchange.getRequest())){
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                };
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader !=null && authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.substring(7);
                } try {
                   template.getForObject("http://localhost:8889/auth/validate-token?token=" + "Bearer " + authHeader, String.class);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não Autorizado!");
                }
            }
            return chain.filter(exchange);
        });
    }
    public static class Config{
    }
}
