package com.vvelikova.ppmtool.payload;


import javax.validation.constraints.NotBlank;

public class LoginRequest {

    // what the client is going to send to the servr - username + password (JSON)

    @NotBlank(message = "Username cannot be blank")
    private String username; // typed just like in User domain
    @NotBlank(message = "Password cannot be blank")
    private String password; // typed just like in User domain

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
}
