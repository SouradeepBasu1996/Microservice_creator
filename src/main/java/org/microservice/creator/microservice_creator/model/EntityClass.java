package org.microservice.creator.microservice_creator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityClass {

    private String entityName;
    private List<EntityModel> fields;
}
