package org.microservice.creator.microservice_creator.service;

import lombok.AllArgsConstructor;
import org.microservice.creator.microservice_creator.model.ProjectConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ControllerService {

    private TemplateProcesorService templateProcesorService;
    private FileService fileService;

    public void createController(ProjectConfig config)throws IOException {
        Path templateControllerPath = Paths.get("src/main/resources/templates/ControllerTemplate.java");
        StringBuilder dependencyBuilder = new StringBuilder();
        String serviceObj = config.getServiceClassName().substring(0, 1).toLowerCase()+config.getServiceClassName().substring(1);
        dependencyBuilder.append("\tprivate ")
                .append(config.getServiceClassName())
                .append(" ")
                .append(serviceObj)
                .append(";\n");
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("packageName", config.getGroupId());
        placeholders.put("packageClass", config.getProjectName());
        placeholders.put("entityName",config.getEntityClass().getEntityName());
        placeholders.put("serviceClassName",config.getServiceClassName());
        placeholders.put("requestMapping",config.getApiURL());
        placeholders.put("controller_name", config.getController());
        placeholders.put("serviceDependency",dependencyBuilder.toString());
        placeholders.put("methods",createCrudMethods(config));

        String processedService = templateProcesorService.processTemplate(templateControllerPath,placeholders);
        Path projectRootPath = Paths.get(System.getProperty("user.home"), "Downloads", config.getProjectName());
        Path serviceClassPath = projectRootPath.resolve("src/main/java/" +
                config.getGroupId().replace('.', '/') + "/" +
                config.getArtifactId() + "/controller/"+config.getController()+".java");
        fileService.saveFile(processedService,serviceClassPath);
    }

    private String  createCrudMethods(ProjectConfig projectConfig){
        return """
                \t@GetMapping("all%1$s/{id}")
                \tpublic ResponseEntity<Optional<%1$s>> get%1$sById(@PathVariable(value = "id") Long %2$s){
                \tOptional<%1$s> %3$s = %6$s.find%1$sById(%2$s);
                \treturn ResponseEntity.ok(%3$s);
                \t}
                \t@GetMapping("/all")
                \tpublic ResponseEntity<List<%1$s>> getAll%1$ss(){
                \tList<%1$s> %5$s = %6$s.findAll%1$ss();
                \treturn ResponseEntity.ok(%5$s);
                \t}
                \t@PostMapping("/add")
                \tpublic String add%1$s(@RequestBody %1$s %3$s){
                \t%1$s %3$sResponse = %6$s.save%1$s(%3$s);
                \treturn "Created"+%3$sResponse;
                \t}
                \t@PutMapping("/update/{id}")
                \tpublic ResponseEntity<String> update%1$s(@PathVariable(value = "id") Long %2$s){
                \treturn ResponseEntity.ok("Updated Successfully");
                \t}
                \t@DeleteMapping("/delete/{id}")
                \tpublic ResponseEntity<String> delete%1$s(@PathVariable(value = "id") Long %2$s){
                \treturn ResponseEntity.ok("Deleted Successfully");
                \t}
                """.formatted(projectConfig.getEntityClass().getEntityName(),
                projectConfig.getEntityClass().getEntityName().toLowerCase()+"Id",
                projectConfig.getEntityClass().getEntityName().toLowerCase(),
                projectConfig.getServiceClassName(),
                projectConfig.getEntityClass().getEntityName().toLowerCase()+"List",
                projectConfig.getServiceClassName().substring(0, 1).toLowerCase()+projectConfig.getServiceClassName().substring(1)
                );
    }
}
