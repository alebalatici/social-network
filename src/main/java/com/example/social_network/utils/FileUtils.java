package com.example.social_network.utils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtils {
    public static void copyTargetTemplate(String targetFileName, String templateFileName) {
        String cleanTemplatePath = templateFileName.startsWith("/") ? templateFileName.substring(1) : templateFileName;
        try (InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(cleanTemplatePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Cannot find template file: " + cleanTemplatePath);
            }

            Files.copy(inputStream, Paths.get(targetFileName), StandardCopyOption.REPLACE_EXISTING);
        }

        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}