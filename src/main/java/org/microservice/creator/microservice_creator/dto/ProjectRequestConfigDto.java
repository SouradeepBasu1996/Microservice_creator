package org.microservice.creator.microservice_creator.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestConfigDto {

    private String projectName;
    private String groupId;
    private String description;
    private List<String> entityList;

}
