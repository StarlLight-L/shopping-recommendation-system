package com.example.backend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir; // File upload directory

    public String storeFile(MultipartFile file) throws IOException {
        // Ensure the upload directory exists
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the upload directory
        }

        // Generate a unique file name to avoid overwriting
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + File.separator + fileName);

        // Save the file
        Files.copy(file.getInputStream(), path);

        // Return the file URL path (assuming a static resource directory)
        return "/uploads/" + fileName;
    }

    public String storeProductImage(MultipartFile file) throws IOException {
        // Directory for storing product images in the frontend project
        String frontendDir = "E:/shopping_master/frontend/public/pictures";

        File directory = new File(frontendDir);
        if (!directory.exists()) {
            directory.mkdirs(); // Ensure the directory exists
        }

        // Prevent duplicate file names
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(frontendDir, fileName);

        // Save the file
        Files.copy(file.getInputStream(), path);

        // Return the relative path (stored in the database)
        return "/pictures/" + fileName;
    }
}

