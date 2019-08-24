package com.vkstech.authorizationserver.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SignUpForm {

    @NotNull(message = "username cannot be null")
    @NotBlank(message = "username cannot be blank")
    @NotEmpty(message = "username cannot be null")
    private String username;

    @NotNull(message = "password cannot be null")
    @NotBlank(message = "password cannot be blank")
    @NotEmpty(message = "password cannot be empty")
    private String password;

    @NotNull(message = "email cannot be null")
    @NotBlank(message = "email cannot be blank")
    @NotEmpty(message = "email cannot be empty")
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
