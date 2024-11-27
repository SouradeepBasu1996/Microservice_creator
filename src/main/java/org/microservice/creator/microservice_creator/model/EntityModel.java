package org.microservice.creator.microservice_creator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntityModel {

   private String fieldName;
   private DataType dataType;
}
