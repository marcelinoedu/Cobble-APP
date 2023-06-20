package com.manager.user.controllers.user;

import com.manager.user.config.security.JwtService;
import com.manager.user.data.UpdateEmailRequest;
import com.manager.user.data.UpdatePasswordRequest;
import com.manager.user.data.UpdateProfileRequest;
import com.manager.user.data.UserRequest;
import com.manager.user.models.User.UserModel;
import com.manager.user.repositories.UserRepository;
import com.manager.user.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UpdateUserInfoController {

    private final JwtService jwtService;

    private final UserService userService;

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UpdateUserInfoController(JwtService jwtService,
                          UserService userService,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest updateProfileRequest, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Long userId = jwtService.extractUserId(token);

        Optional<UserModel> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserModel existingUser = optionalUser.get();

            if (updateProfileRequest.getName() != null) {
                if (updateProfileRequest.getName().equals(existingUser.getName())) {
                    return ResponseEntity.badRequest()
                            .body("Por boas práticas forneça um nome diferente do seu atual!");
                }
                existingUser.setName(updateProfileRequest.getName());
            }

            if (updateProfileRequest.getBirthdate() != null) {
                if (updateProfileRequest.getBirthdate().equals(existingUser.getBirthdate())) {
                    return ResponseEntity.badRequest()
                            .body("Forneça uma data de nascimento diferente da sua atual!");
                }
                existingUser.setBirthdate(updateProfileRequest.getBirthdate());
            }

            userRepository.save(existingUser);
            return ResponseEntity.ok("Suas credenciais foram atualizadas!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado ou não encontrado em nosso sistema");
        }
    }


}

