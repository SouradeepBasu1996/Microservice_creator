package org.microservice.creator.microservice_creator.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileService {

    public void saveFile(String content, Path targetPath) throws IOException {
        Files.createDirectories(targetPath.getParent());
        Files.writeString(targetPath, content);
    }
}
