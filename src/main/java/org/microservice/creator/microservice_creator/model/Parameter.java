package org.microservice.creator.microservice_creator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parameter {

    private String parameterName;
    private String parameterType;
}
