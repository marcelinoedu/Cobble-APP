package com.facade.userfacade.Controllers.User.Auth;

import com.facade.userfacade.data.auth.AuthenticationRequest;
import com.facade.userfacade.data.auth.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
public class LoginController {

    private final RestTemplate restTemplate;
    private final String userPathUrl;

    public LoginController(RestTemplate restTemplate, @Value("${user.path.url}") String userPathUrl) {
        this.restTemplate = restTemplate;
        this.userPathUrl = userPathUrl;
    }
//    @PostMapping("/auth")
//    public ResponseEntity<?> login(@Validated @RequestBody AuthenticationRequest request) {
//        String loginUrl = userPathUrl + "/user/auth";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<AuthenticationRequest> httpEntity = new HttpEntity<AuthenticationRequest>(request, headers);
//
//        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(loginUrl, httpEntity, AuthenticationResponse.class);
//        AuthenticationResponse authenticationResponse = response.getBody();
//        if (response.getStatusCode() == HttpStatus.OK) {
//            if (authenticationResponse != null && authenticationResponse.getToken() != null) {
//                return ResponseEntity.ok(authenticationResponse);
//            }
//        }
//        assert authenticationResponse != null;
//        return ResponseEntity.ok(authenticationResponse.getBody());
//    }
@PostMapping("/auth")
public ResponseEntity<?> login(@Validated @RequestBody AuthenticationRequest request) {
    String loginUrl = userPathUrl + "/user/auth";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<AuthenticationRequest> httpEntity = new HttpEntity<>(request, headers);
    try {
        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(loginUrl, httpEntity, AuthenticationResponse.class);
        AuthenticationResponse authenticationResponse = response.getBody();
        if (response.getStatusCode() == HttpStatus.OK) {
            if (authenticationResponse != null && authenticationResponse.getToken() != null) {
                return ResponseEntity.ok(response.getBody());
            }
        }
    } catch (HttpClientErrorException.Unauthorized ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas!");
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor.");
}

}
