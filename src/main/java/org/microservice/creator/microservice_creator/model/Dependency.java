package org.microservice.creator.microservice_creator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dependency {

    private String dependencyName;
    private String groupId;
    private String artifactId;
    private String version;
}
