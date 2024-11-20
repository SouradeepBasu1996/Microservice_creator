package org.microservice.creator.microservice_creator.entity;

import lombok.*;


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
    private String packaging;


}
