package org.microservice.creator.microservice_creator.service;

import lombok.AllArgsConstructor;
import org.microservice.creator.microservice_creator.model.ProjectConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ServiceLayerService {

    private RepositoryService repositoryService;
    private TemplateProcesorService templateProcesorService;
    private FileService fileService;

    public void createServiceClass(ProjectConfig config)throws IOException {
        //List<String> repositoryList = repositoryService.getRepositories();
        String repositoryName = config.getEntityClass().getEntityName();
        Path templateServiceClassPath = Paths.get("src/main/resources/templates/ServiceClassTemplate.java");
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("packageName", config.getGroupId());
        placeholders.put("packageClass", config.getProjectName());
        placeholders.put("serviceClassName", config.getServiceClassName());

        StringBuilder repoBuilder = new StringBuilder();
        StringBuilder importBuilder = new StringBuilder();
        StringBuilder crudBuilder = new StringBuilder();
        StringBuilder modelImportBuilder = new StringBuilder();


            repoBuilder.append("\tprivate ")
                    .append(repositoryName)
                    .append("Repository")
                    .append(" ")
                    .append(createRepositoryObj(repositoryName))
                    .append("Repository")
                    .append(";\n");

            modelImportBuilder.append("import")
                    .append(" ")
                    .append(config.getGroupId())
                    .append(".")
                    .append(config.getProjectName())
                    .append(".")
                    .append("entity")
                    .append(".")
                    .append(repositoryName)
                    .append(";\n");

            importBuilder.append("import ")
                    .append(config.getGroupId())
                    .append(".")
                    .append(config.getProjectName())
                    .append(".")
                    .append("repository.")
                    .append(repositoryName)
                    .append("Repository")
                    .append(";\n");

            crudBuilder.append(createCRUDMethods(repositoryName));

        placeholders.put("imports",importBuilder.toString());
        placeholders.put("repositories",repoBuilder.toString());
        placeholders.put("crudMethods",crudBuilder.toString());
        placeholders.put("entity_imports",modelImportBuilder.toString());

        String processedService = templateProcesorService.processTemplate(templateServiceClassPath,placeholders);
        Path projectRootPath = Paths.get(System.getProperty("user.home"), "Downloads", config.getProjectName());
        Path serviceClassPath = projectRootPath.resolve("src/main/java/" +
                config.getGroupId().replace('.', '/') + "/" +
                config.getArtifactId() + "/service/"+config.getServiceClassName()+".java");
        fileService.saveFile(processedService,serviceClassPath);
    }

    private String createRepositoryObj(String repository){
        return repository.substring(0,1)
                .toLowerCase()+repository.substring(1);
    }

    private String createCRUDMethods(String entityName){
        String entityObj = entityName.substring(0,1).toLowerCase()+entityName.substring(1);

        return """
            // Create
            public %1$s save%1$s(%1$s %2$s) {
                return %3$s.save(%2$s);
            }
        
            // Read All
            public List<%1$s> findAll%1$ss() {
                return %3$s.findAll();
            }
        
            // Read by ID
            public Optional<%1$s> find%1$sById(Long id) {
                return %3$s.findById(id);
            }
        
            // Update
            public %1$s update%1$s(%1$s %2$s) {
                if (%3$s.existsById(%2$s.getId())) {
                    return %3$s.save(%2$s);
                }
                throw new IllegalArgumentException("Entity not found");
            }
        
            // Delete
            public void delete%1$sById(Long id) {
                %3$s.deleteById(id);
            }
         """.formatted(entityName, entityObj, entityObj+"Repository");
    }
}
