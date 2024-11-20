package org.microservice.creator.microservice_creator.service;

import lombok.AllArgsConstructor;
import org.microservice.creator.microservice_creator.dto.ProjectRequestConfigDto;
import org.microservice.creator.microservice_creator.entity.BuildTool;
import org.microservice.creator.microservice_creator.entity.ProjectConfig;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@AllArgsConstructor
public class ProjectService {

    private TemplateProcesorService templateProcesorService;
    private FileService fileService;

    public File createProject(ProjectRequestConfigDto projectRequest)throws IOException{

        ProjectConfig config = projectConfigMapper(projectRequest);

        String downloadsFolder = System.getProperty("user.home") + "/Downloads";

        File projectDir = new File(downloadsFolder, config.getProjectName());

        if (!projectDir.exists() && !projectDir.mkdirs()) {
            throw new IOException("Failed to create project directory: " + projectDir.getAbsolutePath());
        }

        createBasicStructure(projectDir);
        createMainClass(config);
        createPom(config);

        return zipProject(projectDir);
    }

    private void createBasicStructure(File projectDir)throws IOException{
        String[] paths = {
                "src/main/java",
                "src/main/resources",
                "src/test/java",
                "src/test/resources"
        };

        for (String path : paths) {
            File dir = new File(projectDir, path);
            if (!dir.mkdirs()) {
                throw new IOException("Failed to create directory: " + dir.getAbsolutePath());
            }
        }
    }
    private void createPom(ProjectConfig config)throws IOException{
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("groupId", config.getGroupId());
        placeholders.put("artifactId", config.getArtifactId());
        placeholders.put("description", config.getDescription());

        Path templatePomPath = Paths.get("src/main/resources/templates/pom-template.xml");
        String processedPom = templateProcesorService.processTemplate(templatePomPath, placeholders);

        Path targetPath = Paths.get(System.getProperty("user.home"),
                "Downloads",
                config.getProjectName(),
                "pom.xml");


        fileService.saveFile(processedPom, targetPath);

    }

    private void createMainClass(ProjectConfig config)throws IOException{
        String className = config.getProjectName()
                .substring(0,1)
                .toUpperCase()+config.getProjectName()
                .substring(1)+"Application";

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("packageName", config.getGroupId());
        placeholders.put("className", className);
        placeholders.put("packageClass", config.getProjectName());

        Path templateMainClassPath = Paths.get("src/main/resources/templates/MainClassTemplate.java");
        String processedMainClass = templateProcesorService.processTemplate(templateMainClassPath,placeholders);

        Path projectRootPath = Paths.get(System.getProperty("user.home"), "Downloads", config.getProjectName());
        Path mainClassPath = projectRootPath.resolve("src/main/java/" +
                config.getGroupId().replace('.', '/') + "/" +
                config.getArtifactId() + "/"+className+".java");

        fileService.saveFile(processedMainClass,mainClassPath);
    }

    private File zipProject(File projectDir) throws IOException {
        File zipFile = new File(projectDir.getParent(), projectDir.getName() + ".zip");

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            Path sourcePath = projectDir.toPath();

            Files.walk(sourcePath).forEach(path -> {
                try {
                    String zipEntryName = sourcePath.relativize(path).toString();
                    if (Files.isDirectory(path)) {
                        if (!zipEntryName.isEmpty()) {
                            zos.putNextEntry(new ZipEntry(zipEntryName + "/"));
                            zos.closeEntry();
                        }
                    } else {
                        zos.putNextEntry(new ZipEntry(zipEntryName));
                        Files.copy(path, zos);
                        zos.closeEntry();
                    }
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }

        return zipFile;
    }

    private void deleteDirectory(File directory) throws IOException {
        if (directory.isDirectory()) {
            
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        if (!directory.delete()) {
            throw new IOException("Failed to delete file: " + directory.getAbsolutePath());
        }
    }
    private ProjectConfig projectConfigMapper(ProjectRequestConfigDto projectRequest){

        return ProjectConfig.builder()
                .projectName(projectRequest.getProjectName())
                .artifactId(projectRequest.getProjectName())
                .description(projectRequest.getDescription())
                .groupId(projectRequest.getGroupId())
                .buildTool(BuildTool.MAVEN)
                .packaging("Jar")
                .build();
    }
}
