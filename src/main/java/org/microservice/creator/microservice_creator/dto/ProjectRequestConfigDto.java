package org.microservice.creator.microservice_creator.dto;

import lombok.*;
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
    private List<Map<String,List<EntityModel>>> entities;

}
