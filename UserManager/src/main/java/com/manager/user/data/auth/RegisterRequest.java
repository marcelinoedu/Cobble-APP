package com.manager.user.data.auth;

import com.manager.user.models.User.UserModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequest {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String password;
    private LocalDate birthdate;


    public RegisterRequest(Long id, String name, String email, String cpf, String password, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
        this.birthdate = birthdate;
    }

    public RegisterRequest(UserModel user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.cpf = user.getCpf();
        this.password = user.getPassword();
        this.birthdate = user.getBirthdate();
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", password='" + password + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}
