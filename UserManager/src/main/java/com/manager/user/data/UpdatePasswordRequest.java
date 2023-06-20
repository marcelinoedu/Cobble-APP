package com.manager.user.data;

import brave.internal.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class UpdatePasswordRequest {

    private String password;

    private String confirmPassword;

    public UpdatePasswordRequest(){

    }
    public UpdatePasswordRequest(String password, String confirmPassword) {
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
