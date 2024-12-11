package org.microservice.creator.microservice_creator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ControllerSpecs {

    private String controllerName;
    private List<ControllerMethodSpecs> methodSpecs;

}
