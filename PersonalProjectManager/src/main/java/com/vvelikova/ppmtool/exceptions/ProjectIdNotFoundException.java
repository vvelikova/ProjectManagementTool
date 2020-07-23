package com.vvelikova.ppmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectIdNotFoundException extends RuntimeException{

    public ProjectIdNotFoundException(String message) {
        super(message);
    }
}
