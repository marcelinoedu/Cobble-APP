package com.manager.user.controllers.user;


import com.manager.user.config.security.JwtService;
import com.manager.user.data.UserRequest;
import com.manager.user.models.User.UserModel;
import com.manager.user.repositories.UserRepository;
import com.manager.user.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class GetUserInfoController {
    private final JwtService jwtService;

    private final UserService userService;

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;
    public GetUserInfoController(JwtService jwtService,
                                    UserService userService,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/info")
    public ResponseEntity<?> userRequest(HttpServletRequest request, UserRequest userInfo) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Long userId = jwtService.extractUserId(token);

        Optional<UserModel> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            UserModel user = optionalUser.get();
            userInfo.setId(user.getId());
            userInfo.setName(user.getName());
            userInfo.setEmail(user.getEmail());
            userInfo.setCpf(user.getCpf());
            userInfo.setBirthdate(user.getBirthdate());

            return ResponseEntity.ok(userInfo);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Usuário não autenticado ou não encontrado em nosso sistema");
    }

}
