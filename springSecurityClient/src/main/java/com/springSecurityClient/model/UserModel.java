package com.springSecurityClient.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String matchingPassword;
}
