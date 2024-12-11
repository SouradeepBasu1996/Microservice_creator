package org.microservice.creator.microservice_creator.dto;

import lombok.*;
import org.microservice.creator.microservice_creator.model.ControllerSpecs;
import org.microservice.creator.microservice_creator.model.DatabaseSpecs;
import org.microservice.creator.microservice_creator.model.EntityClass;
import org.microservice.creator.microservice_creator.model.EntityModel;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestConfigDto {

    private String projectName;
    private String groupId;
    private String description;
    private String controller;
    private String apiURL;
    private EntityClass entityClass;
    private DatabaseSpecs databaseSpecs;
    private String serviceClassName;

}
