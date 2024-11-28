package org.microservice.creator.microservice_creator.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatabaseSpecs {

    private String databaseName;
    private String userName;
    private String password;
}
