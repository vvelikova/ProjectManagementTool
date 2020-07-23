package com.vvelikova.ppmtool.payload;

public class JWTloginSucessResponse {

    // when we have a valid user and return JWT
    // we will build response that we can pass on to the React side of the application

    private boolean success;
    private String token;

    public JWTloginSucessResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "JWTloginSucessResponse{" +
                "success=" + success +
                ", token='" + token + '\'' +
                '}';
    }
}
