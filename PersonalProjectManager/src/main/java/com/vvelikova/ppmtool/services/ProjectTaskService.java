package com.vvelikova.ppmtool.services;

import com.vvelikova.ppmtool.domain.Backlog;
import com.vvelikova.ppmtool.domain.Project;
import com.vvelikova.ppmtool.domain.ProjectTask;
import com.vvelikova.ppmtool.exceptions.ProjectIdNotFoundException;
import com.vvelikova.ppmtool.repositories.BackLogRepository;
import com.vvelikova.ppmtool.repositories.ProjectRepository;
import com.vvelikova.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {
    @Autowired
    private BackLogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {


//    try{
        //PTs to be added to a specific project, project not null, BL exists\
        Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog(); //backlogRepository.findByProjectIdentifier(projectIdentifier);
        // set the backlog to the project task
        projectTask.setBacklog(backlog);

        //we want our project sequence to be like this -> IDPR0-1
        Integer BacklogSequence = backlog.getPTSequence();
        // update the backlog sequence
        BacklogSequence ++;
        backlog.setPTSequence(BacklogSequence);
        //Add sequence to project task
        projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + BacklogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);

        //initial priority when priority null
        if(projectTask.getPriority() == null || projectTask.getPriority() == 0) {
            projectTask.setPriority(3);
        }

        //initial status when status is null
        if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
//        if(projectTask.getStatus() == null) {
            projectTask.setStatus("To-Do");
        }

        return projectTaskRepository.save(projectTask);
//    }catch (Exception e) {
//        throw new ProjectIdNotFoundException("No such project found!");
//    }


    }

    public Iterable<ProjectTask> findBacklogById(String id, String username) {

        projectService.findProjectByIdentifier(id, username);

//        Project project = projectRepository.findByProjectIdentifier(id);

//        if(project == null) {
//            throw new ProjectIdNotFoundException("Project with id: " + id + " does not exist.");
//        }
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
     }

     public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {

        // makes sure we are searching on the existing backlog // existing Project
//         Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
//         if(backlog == null) {
//             throw new ProjectIdNotFoundException("Project with id: " + backlog_id + " does not exist.");
//         }
         projectService.findProjectByIdentifier(backlog_id, username);


         //make sure the task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
         if(projectTask == null) {
             throw new ProjectIdNotFoundException("Project task " + pt_id + " not found.");
         }

         //make sure the backlog/project id in the path corresponds to the right project
         if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
             throw new ProjectIdNotFoundException("Project task " + pt_id + " does not exist in project " + backlog_id);
         }

        return projectTask;
     }


     // update project task
    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){
        //find existing project task
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
        projectTask.setBacklog(null);

        // replace it with the updated task
        projectTask = updatedTask;

        //save update
        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
        ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);

//        Backlog backlog = projectTask.getBacklog();
//        List<ProjectTask> pr = backlog.getProjectTasks();
//        pr.remove(projectTask);
//        List<ProjectTask> after = backlog.getProjectTasks();

        projectTaskRepository.delete(projectTask);
    }

}
