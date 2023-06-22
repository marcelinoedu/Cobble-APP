package com.facade.userfacade.Controllers.User.Info;

import com.facade.userfacade.data.User.Info.UserRequest;
import com.facade.userfacade.data.User.Info.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
public class GetUserInfoController {
    private final RestTemplate restTemplate;

    private final String userPathUrl;


    public GetUserInfoController(RestTemplate restTemplate, @Value("${user.path.url}") String userPathUrl) {
        this.restTemplate = restTemplate;
        this.userPathUrl = userPathUrl;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String userInfoUrl = userPathUrl + "/user/info";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorization);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        try{
            ResponseEntity<UserResponse> response = restTemplate.exchange(userInfoUrl,
                    HttpMethod.GET,
                    httpEntity,
                    UserResponse.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                UserResponse userInfo = response.getBody();
                if (userInfo != null) {
                    return ResponseEntity.ok(userInfo);
                }
            }
        } catch (HttpClientErrorException.Unauthorized ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado ou não encontrado em nosso sistema");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor.");
    }


}
