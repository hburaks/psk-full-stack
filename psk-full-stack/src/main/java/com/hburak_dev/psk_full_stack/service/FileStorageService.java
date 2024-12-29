package com.hburak_dev.psk_full_stack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.net.MalformedURLException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

  @Value("${application.file.upload-dir:uploads}")
  private String baseUploadDir;

  @PostConstruct
  public void init() {
    try {
      // Create base directory
      Files.createDirectories(Path.of(baseUploadDir));
      // Create subdirectories
      Files.createDirectories(Path.of(baseUploadDir, "blogs"));
      Files.createDirectories(Path.of(baseUploadDir, "tests"));
    } catch (IOException e) {
      log.error("Could not create upload directories", e);
      throw new RuntimeException("Could not create upload directories!");
    }
  }

  public String storeFile(MultipartFile file, String subDirectory) {
    String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
    String fileName = System.currentTimeMillis() + "_" + originalFileName;

    try {
      if (file.isEmpty()) {
        throw new RuntimeException("Failed to store empty file " + fileName);
      }

      // Check for invalid characters in filename
      if (fileName.contains("..")) {
        throw new RuntimeException("Filename contains invalid path sequence " + fileName);
      }

      // Copy file to target location
      Path targetLocation = Path.of(baseUploadDir, subDirectory).resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      log.info("Stored file: {} in directory: {}", fileName, subDirectory);
      return fileName;

    } catch (IOException e) {
      log.error("Failed to store file: {} in directory: {}", fileName, subDirectory, e);
      throw new RuntimeException("Failed to store file " + fileName);
    }
  }

  public Resource loadFileAsResource(String fileName, String subDirectory) {
    try {
      Path filePath = Path.of(baseUploadDir, subDirectory).resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists()) {
        return resource;
      } else {
        log.error("File not found: {} in directory: {}", fileName, subDirectory);
        throw new RuntimeException("File not found " + fileName);
      }
    } catch (MalformedURLException e) {
      log.error("File not found: {} in directory: {}", fileName, subDirectory, e);
      throw new RuntimeException("File not found " + fileName);
    }
  }

  public void deleteFile(String fileName, String subDirectory) {
    try {
      Path filePath = Path.of(baseUploadDir, subDirectory).resolve(fileName).normalize();
      Files.deleteIfExists(filePath);
      log.info("Deleted file: {} from directory: {}", fileName, subDirectory);
    } catch (IOException e) {
      log.error("Error deleting file: {} from directory: {}", fileName, subDirectory, e);
      throw new RuntimeException("Could not delete file " + fileName);
    }
  }
}