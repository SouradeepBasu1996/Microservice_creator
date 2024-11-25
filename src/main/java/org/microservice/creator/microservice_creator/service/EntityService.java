package org.microservice.creator.microservice_creator.service;

import lombok.AllArgsConstructor;
import org.microservice.creator.microservice_creator.model.ProjectConfig;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EntityService {

    private TemplateProcesorService templateProcesorService;
    private FileService fileService;

    public void createEntityDir(File projectDir, ProjectConfig config)throws IOException{
        String path = "src/main/java/"+config.getGroupId().replace('.', '/') + "/" +
                config.getArtifactId() + "/entity";

        File dir = new File(projectDir,path);
        if (!dir.mkdirs()) {
            throw new IOException("Failed to create directory: " + dir.getAbsolutePath());
        }
        config.getEntity().forEach(entity-> {
            try {
                createEntity(entity,config);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void createEntity(String entityName,ProjectConfig config)throws IOException{
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("packageName", config.getGroupId());
        placeholders.put("packageClass", config.getProjectName());
        placeholders.put("entityName",entityName);

        Path templateEntityPath = Paths.get("src/main/resources/templates/EntityTemplate.java");
        String processedEntity = templateProcesorService.processTemplate(templateEntityPath,placeholders);

        Path projectRootPath = Paths.get(System.getProperty("user.home"), "Downloads", config.getProjectName());
        Path entityClassPath = projectRootPath.resolve("src/main/java/" +
                config.getGroupId().replace('.', '/') + "/" +
                config.getArtifactId() + "/entity/"+entityName+".java");

        fileService.saveFile(processedEntity,entityClassPath);
    }

}
