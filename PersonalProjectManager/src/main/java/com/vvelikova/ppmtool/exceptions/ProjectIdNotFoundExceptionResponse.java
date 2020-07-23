package com.vvelikova.ppmtool.exceptions;

public class ProjectIdNotFoundExceptionResponse {

    private String projectNotFound; // this wil be the key in the json response

    public ProjectIdNotFoundExceptionResponse(String projectNotFound) {
        this.projectNotFound = projectNotFound;
    }

    public String getProjectNotFound() {
        return projectNotFound;
    }

    public void setProjectNotFound(String projectNotFound) {
        this.projectNotFound = projectNotFound;
    }
}
