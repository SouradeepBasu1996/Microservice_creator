package org.microservice.creator.microservice_creator.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectConfig {

    private String projectName;
    private String groupId;
    private String artifactId;
    private BuildTool buildTool;
    private String description;
    private List<String> entity;


}
