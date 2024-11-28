package org.microservice.creator.microservice_creator.service;

import lombok.AllArgsConstructor;
import org.microservice.creator.microservice_creator.model.ProjectConfig;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class DatabaseService {

    private TemplateProcesorService templateProcesorService;
    private FileService fileService;

    public void fillDatabaseSpecs(File projectDir,ProjectConfig projectConfig)throws IOException {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("database_name", projectConfig.getDatabaseSpecs().getDatabaseName());
        placeholders.put("user_name", projectConfig.getDatabaseSpecs().getUserName());
        placeholders.put("password", projectConfig.getDatabaseSpecs().getPassword());


        Path templatePropertiesFile = Paths.get("src/main/resources/templates/temp_application.properties");
        String processedPropertiesFile = templateProcesorService.processTemplate(templatePropertiesFile,placeholders);

        Path projectRootPath = Paths.get(System.getProperty("user.home"), "Downloads", projectConfig.getProjectName());
        Path propertiesFilePath = projectRootPath.resolve("src/main/resources/application.properties");

        fileService.saveFile(processedPropertiesFile,propertiesFilePath);
    }
}
