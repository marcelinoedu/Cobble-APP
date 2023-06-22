package com.facade.userfacade.data.User.Info;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private LocalDate birthdate;

    public UserResponse() {
    }

    public UserResponse(Long id, String name, String email, String cpf, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.birthdate = birthdate;
    }
}
