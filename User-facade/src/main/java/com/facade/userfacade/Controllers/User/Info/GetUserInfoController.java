package com.facade.userfacade.Controllers.User.Info;

import com.facade.userfacade.data.UserRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
public class GetUserInfoController {
    private final RestTemplate restTemplate;

    public GetUserInfoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String userInfoUrl = "http://localhost:8080/user/info";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorization);

        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<UserRequest> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, httpEntity, UserRequest.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            UserRequest userInfo = response.getBody();
            if (userInfo != null) {
                return ResponseEntity.ok(userInfo);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Usuário não autenticado ou não encontrado em nosso sistema");
    }


}
