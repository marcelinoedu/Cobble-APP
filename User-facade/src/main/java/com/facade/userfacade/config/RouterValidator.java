package com.facade.userfacade.config;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class RouterValidator {

    private static final List<String> openApiEndpoints = List.of(
            "/user/auth",
            "/user/auth/register",
            "/user/auth/validate-token",
            "/eureka"
    );

    public boolean isSecured(String path) {
        return openApiEndpoints.stream().noneMatch(path::startsWith);
    }
}
