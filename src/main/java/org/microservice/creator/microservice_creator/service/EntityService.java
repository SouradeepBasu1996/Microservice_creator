package org.microservice.creator.microservice_creator.service;

import lombok.AllArgsConstructor;
import org.microservice.creator.microservice_creator.model.EntityModel;
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
    private DataTypeService dataTypeService;
    private RepositoryService repositoryService;

    public void createEntityDir(File projectDir, ProjectConfig config)throws IOException{
        String path = "src/main/java/"+config.getGroupId().replace('.', '/') + "/" +
                config.getArtifactId() + "/entity";

        File dir = new File(projectDir,path);
        if (!dir.mkdirs()) {
            throw new IOException("Failed to create directory: " + dir.getAbsolutePath());
        }

        createEntity(config.getEntities(),config);
    }

    private void createEntity(Map<String,List<EntityModel>> entityMap, ProjectConfig config)throws IOException{

        Path templateEntityPath = Paths.get("src/main/resources/templates/EntityTemplate.java");
        for (Map.Entry<String,List<EntityModel>> entity:entityMap.entrySet()){
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("packageName", config.getGroupId());
            placeholders.put("packageClass", config.getProjectName());
            placeholders.put("entityName",entity.getKey());
            placeholders.put("tableName",formatSnakeCase(entity.getKey()));

            StringBuilder fieldsBuilder = new StringBuilder();
            entity.getValue().forEach(entityModel -> {
                fieldsBuilder.append("    @Column(name =\"")
                        .append(formatSnakeCase(entityModel.getFieldName()))
                        .append("\")\n")
                        .append("    ")
                        .append("private ")
                        .append(dataTypeService.resolveDataType(entityModel.getDataType()))
                        .append(" ")
                        .append(entityModel.getFieldName())
                        .append(";\n");
            });
            placeholders.put("fields", fieldsBuilder.toString());
            String processedEntity = templateProcesorService.processTemplate(templateEntityPath,placeholders);
            Path projectRootPath = Paths.get(System.getProperty("user.home"), "Downloads", config.getProjectName());
            Path entityClassPath = projectRootPath.resolve("src/main/java/" +
                    config.getGroupId().replace('.', '/') + "/" +
                    config.getArtifactId() + "/entity/"+entity.getKey()+".java");
            fileService.saveFile(processedEntity,entityClassPath);

            repositoryService.createRepo(entity.getKey(),config);
        }
    }

    private String formatSnakeCase(String fieldName){

        String str = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);

        StringBuilder tableName = new StringBuilder();

        for(int index = 0; index<str.length();index++){
            char hump =str.charAt(index);

            if(Character.isUpperCase(hump)){
                if(index>0)
                    tableName.append('_');
                tableName.append(Character.toLowerCase(hump));
            }
            else{
                tableName.append(hump);
            }
        }

        return tableName.toString();
    }
}
