package com.vvelikova.ppmtool.exceptions;

public class UsernameAlreadyExistsReposnse {

    private String usernameAlreadyExists;

    public UsernameAlreadyExistsReposnse(String usernameAlreadyExists) {
        this.usernameAlreadyExists = usernameAlreadyExists;
    }

    public String getUsernameAlreadyExists() {
        return usernameAlreadyExists;
    }

    public void setUsernameAlreadyExists(String usernameAlreadyExists) {
        this.usernameAlreadyExists = usernameAlreadyExists;
    }
}
