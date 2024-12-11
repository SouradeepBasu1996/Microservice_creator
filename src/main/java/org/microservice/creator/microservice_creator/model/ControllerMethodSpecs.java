package org.microservice.creator.microservice_creator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControllerMethodSpecs {

    private HttpMethod httpMethod;
    private String methodName;
    private Parameter parameters;
    private String returnType;
    private String endPoint;
}
