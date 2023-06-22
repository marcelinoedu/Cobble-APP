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
public class UserRequest {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private LocalDate birthdate;

    public UserRequest() {
    }

    public UserRequest(Long id, String name, String email, String cpf, LocalDate birthdate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.birthdate = birthdate;
    }
}
