package com.manager.user.data;

import brave.internal.Nullable;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
public class UpdateEmailRequest {
    @Nullable
    private String email;

    public UpdateEmailRequest(){

    }

    public UpdateEmailRequest(String email) {
        this.email = email;
    }
}
