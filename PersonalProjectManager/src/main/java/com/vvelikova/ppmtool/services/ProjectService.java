package com.vvelikova.ppmtool.services;


import com.vvelikova.ppmtool.domain.Backlog;
import com.vvelikova.ppmtool.domain.Project;
import com.vvelikova.ppmtool.domain.User;
import com.vvelikova.ppmtool.exceptions.ProjectIdException;
import com.vvelikova.ppmtool.exceptions.ProjectIdNotFoundException;
import com.vvelikova.ppmtool.repositories.BackLogRepository;
import com.vvelikova.ppmtool.repositories.ProjectRepository;
import com.vvelikova.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BackLogRepository backLogRepository;

    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject (Project project, String username) {

        if(project.getId() != null) {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

            if(existingProject != null && (!existingProject.getUser().getUsername().equals(username))) { // OR !existingProject.getProjectLeader.equals(username)
                throw new ProjectIdNotFoundException("Project not found in your account!");
            } else if (existingProject == null) {
                throw new ProjectIdNotFoundException("Project with ID " + project.getProjectIdentifier() + " cannot be updated. It does not exists!");
            }
        }

        try{
            User user = userRepository.findByUsername(username);
            project.setUser(user); // one to many relationship (one user, many projects)
            project.setProjectLeader(user.getUsername());

            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            if(project.getId() == null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId() != null) {
                project.setBacklog(backLogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }
            return projectRepository.save(project);

        } catch ( Exception e) {
            throw new ProjectIdException("Project Id " + project.getProjectIdentifier().toUpperCase() + " Already exists.");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null ) {
            throw new ProjectIdException("Project Id: " + projectId + " does not exist in the db.");
        }

        // check if the user holds that project
        if(!project.getProjectLeader().equals(username)) {
            throw new ProjectIdNotFoundException("Project not found in your account.");
        }

        return project;
    }

    public Iterable<Project> findAllProjects(String username) {
        return  projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectId, String name) {
//        Project project = projectRepository.findByProjectIdentifier(projectId);
//
//        if(project == null ) {
//            throw new ProjectIdException("Such project id is not present in the db: " + projectId);
//        }

        projectRepository.delete(findProjectByIdentifier(projectId,name )); // delete method is coming out of the box with CrudRepository
    }
}
