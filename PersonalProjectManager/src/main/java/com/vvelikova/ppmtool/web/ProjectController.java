package com.vvelikova.ppmtool.web;


import com.vvelikova.ppmtool.domain.Project;
import com.vvelikova.ppmtool.domain.ProjectTask;
import com.vvelikova.ppmtool.exceptions.ProjectIdException;
import com.vvelikova.ppmtool.services.MapValidationErrorService;
import com.vvelikova.ppmtool.services.ProjectService;
import com.vvelikova.ppmtool.services.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectTaskService projectTaskService;

    @Autowired
    private MapValidationErrorService mvs;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project,
                                                    BindingResult result, Principal principal) {
        //principal - the person who is logged in / owner of the token/

        ResponseEntity<?> erroMap = mvs.MapValidationService(result);
        if(erroMap != null) {
            return erroMap;
        }
        Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById (@PathVariable String projectId, Principal principal) {
       Project project = projectService.findProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal) {
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
        projectService.deleteProjectByIdentifier(projectId.toUpperCase(), principal.getName());

        return new ResponseEntity<String>("Project with ID: " + projectId + " was deleted.", HttpStatus.OK);
    }


}
