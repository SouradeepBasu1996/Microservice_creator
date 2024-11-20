package org.microservice.creator.microservice_creator.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestConfigDto {

    private String projectName;
    private String groupId;
    private String description;
}
