package com.facade.userfacade.data;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class UpdateEmailRequest {
    private String email;

    public UpdateEmailRequest(){

    }

    public UpdateEmailRequest(String email) {
        this.email = email;
    }
}
