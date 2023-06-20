package com.facade.userfacade.data;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
public class UpdateProfileRequest {


    private String name;


    private LocalDate birthdate;

    public UpdateProfileRequest() {
    }

    public UpdateProfileRequest(String name, LocalDate birthdate) {
        this.name = name;
        this.birthdate = birthdate;
    }
}

