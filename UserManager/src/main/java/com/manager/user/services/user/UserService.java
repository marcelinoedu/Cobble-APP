package com.manager.user.services.user;

import com.manager.user.data.UserRequest;
import com.manager.user.data.auth.AuthenticationRequest;
import com.manager.user.models.User.UserModel;
import com.manager.user.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateLastLogin(UserModel user) {
        user.setLast_time_login(LocalDateTime.now());
        userRepository.save(user);
    }
    public UserModel getUserById(Long userId) {
        Optional<UserModel> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
    }
    public void updateLastLogout(Long userId) {
        Optional<UserModel> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            UserModel user = optionalUser.get();
            user.setLast_time_logout(LocalDateTime.now());
            userRepository.save(user);
        } else {
            throw new RuntimeException("Usuário não encontrado.");
        }
    }

    public Optional<UserModel> findUser(AuthenticationRequest request) {
        String email = request.getEmail();
        String cpf = request.getCpf();

        if (email != null) {
            try {
                return userRepository.findByEmail(email);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (cpf != null) {
            try {
                return userRepository.findByCpf(cpf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }


}
