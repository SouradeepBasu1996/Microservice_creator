package org.microservice.creator.microservice_creator.service;

import lombok.AllArgsConstructor;
import org.microservice.creator.microservice_creator.model.ProjectConfig;
import org.springframework.stereotype.Service;
import org.w3c.dom.ls.LSInput;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RepositoryService {

    private TemplateProcesorService templateProcesorService;
    private FileService fileService;
    List<String> repoList;

    public void createRepo(String modelClassName,ProjectConfig config)throws IOException {
        String path = "src/main/java/"+config.getGroupId().replace('.', '/') + "/" +
                config.getArtifactId() + "/repository";

        Path templateEntityPath = Paths.get("src/main/resources/templates/RepositoryTemplate.java");

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("packageName", config.getGroupId());
        placeholders.put("packageClass", config.getProjectName());
        placeholders.put("model_name", modelClassName);
        String processedRepo = templateProcesorService.processTemplate(templateEntityPath,placeholders);
        Path projectRootPath = Paths.get(System.getProperty("user.home"), "Downloads", config.getProjectName());
        Path repoClassPath = projectRootPath.resolve(path+"/"+modelClassName+"Repository.java");
        repoList.add(modelClassName);
        fileService.saveFile(processedRepo,repoClassPath);
    }
    public List<String> getRepositories(){

        return repoList;
    }
}
