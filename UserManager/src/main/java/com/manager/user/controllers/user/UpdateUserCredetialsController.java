package com.manager.user.controllers.user;

import com.manager.user.config.security.JwtService;
import com.manager.user.data.UpdateEmailRequest;
import com.manager.user.data.UpdatePasswordRequest;
import com.manager.user.models.User.UserModel;
import com.manager.user.repositories.UserRepository;
import com.manager.user.services.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UpdateUserCredetialsController {

    private final JwtService jwtService;

    private final UserService userService;

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UpdateUserCredetialsController(JwtService jwtService,
                                    UserService userService,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PutMapping("/email")
    public ResponseEntity<?> updateEmail(@RequestBody UpdateEmailRequest updateEmailRequest,
                                         HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Long userId = jwtService.extractUserId(token);

        Optional<UserModel> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserModel existingUser = optionalUser.get();

            if (!updateEmailRequest.getEmail().isEmpty()) {
                if (updateEmailRequest.getEmail().equals(existingUser.getEmail())) {
                    return ResponseEntity.badRequest().body("Forneça um E-mail diferente do seu atual!");
                }
                existingUser.setEmail(updateEmailRequest.getEmail());
                userRepository.save(existingUser);
                return ResponseEntity.ok("E-mail Atualizado com sucesso!");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Preencha o campo 'Novo E-mail' com um E-mail válido.");

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Usuário não autenticado ou não encontrado em nosso sistema");

    }

    @PutMapping("/password")
    public ResponseEntity<?> updadePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest,
                                            HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        Long userId = jwtService.extractUserId(token);

        Optional<UserModel> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserModel existingUser = optionalUser.get();

            if (updatePasswordRequest.getPassword() == null || !updatePasswordRequest.getPassword().equals(updatePasswordRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("As senhas devem ser iguais");
            }

            if (passwordEncoder.matches(updatePasswordRequest.getPassword(), existingUser.getPassword())) {
                return ResponseEntity.badRequest().body("A nova senha tem que ser diferente da atual");
            }
            existingUser.setPassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));
            userRepository.save(existingUser);
            return ResponseEntity.ok("Senha atualizada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário não autenticado ou não encontrado em nosso sistema");
        }
    }

}
