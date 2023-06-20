package com.manager.user.controllers.auth;


import com.manager.user.data.auth.RegisterRequest;
import com.manager.user.models.User.UserModel;
import com.manager.user.repositories.UserRepository;
import com.manager.user.services.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class RegisterController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> createUser(@Validated @RequestBody RegisterRequest registerRequest)
    {
        Optional<UserModel> existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if(existingUser.isPresent()){
            return ResponseEntity.badRequest().body("O email já esta em uso");
        }
        existingUser = userRepository.findByCpf(registerRequest.getCpf());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("O CPF já esta em uso");
        }
        UserModel user = new UserModel();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setCpf(registerRequest.getCpf());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setBirthdate(registerRequest.getBirthdate());
        user.setRole("USER");
        userRepository.save(user);

        return ResponseEntity.ok().body("Cadastro realizado com sucesso!");

    }
}

