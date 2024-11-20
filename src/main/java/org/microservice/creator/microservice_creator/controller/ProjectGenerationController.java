package org.microservice.creator.microservice_creator.controller;

import lombok.AllArgsConstructor;
import org.microservice.creator.microservice_creator.dto.ProjectRequestConfigDto;
import org.microservice.creator.microservice_creator.service.ProjectService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ProjectGenerationController {

    private ProjectService projectService;

    @PostMapping("/generate")
    public String generateProject(@RequestBody ProjectRequestConfigDto projectRequestConfigDto){
        try{
            projectService.createProject(projectRequestConfigDto);
            return "Project structure created successfully!";
        } catch (IOException e) {
            return "Project Creation failed"+e.getMessage();

        }

    }
}
