package org.microservice.creator.microservice_creator.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Service
public class TemplateProcesorService {

    public String processTemplate(Path templatePath, Map<String, String> placeholders)throws IOException {
        String content = Files.readString(templatePath);

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        return content;
    }
}
