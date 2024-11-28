package org.microservice.creator.microservice_creator.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


import java.util.List;
import java.util.Map;


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
    private String controller;
    private Map<String,List<EntityModel>> entities;
    private DatabaseSpecs databaseSpecs;


}
