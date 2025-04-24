package com.springSecurityClient.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenNotFoundException extends RuntimeException{

    private String message;

    public TokenNotFoundException(String message){
        super();
        this.message = message;
    }
}
