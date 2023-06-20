package com.manager.user.controllers.auth;

import com.manager.user.config.security.JwtService;
import com.manager.user.data.auth.AuthenticationRequest;
import com.manager.user.data.auth.AuthenticationResponse;
import com.manager.user.models.User.UserModel;
import com.manager.user.repositories.TokenRepository;
import com.manager.user.repositories.UserRepository;
import com.manager.user.services.token.TokenService;
import com.manager.user.services.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class AuthController {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final JwtService jwtService;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    private final  UserService userService;


    public AuthController(UserRepository userRepository,
                          TokenRepository tokenRepository,
                          JwtService jwtService,
                          TokenService tokenService,
                          PasswordEncoder passwordEncoder,
                          UserService userService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody AuthenticationRequest request,
                                              HttpServletResponse response) {

        Optional<UserModel> optionalUser = userService.findUser(request);
        if (optionalUser.isPresent()) {
            UserModel user = optionalUser.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                tokenService.revokeAllUserTokens(user);
                String jwtToken = jwtService.generateToken(user);
                userService.updateLastLogin(user);
                tokenRepository.save(tokenService.buildToken(jwtToken, user));
                return ResponseEntity.ok(AuthenticationResponse.builder().token(jwtToken).build());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas!");
    }

}
