package com.hburak_dev.psk_full_stack.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

  @Value("${application.file.upload-dir:uploads}")
  private String baseUploadDir;

  @PostConstruct
  public void init() {
    try {
      createDirectoryIfNotExists(Path.of(baseUploadDir));
      createDirectoryIfNotExists(Path.of(baseUploadDir, "blogs"));
      createDirectoryIfNotExists(Path.of(baseUploadDir, "test-templates"));
      createDirectoryIfNotExists(Path.of(baseUploadDir, "comments"));
    } catch (AccessDeniedException e) {
      log.error("Permission denied while creating upload directories", e);
      throw new RuntimeException(
          "Permission denied while creating upload directories. Please check file system permissions.");
    } catch (IOException e) {
      log.error("Could not create upload directories", e);
      throw new RuntimeException("Failed to initialize storage system. Please check system configuration.");
    }
  }

  private void createDirectoryIfNotExists(Path path) throws IOException {
    if (!Files.exists(path)) {
      try {
        Files.createDirectories(path);
        // Verify write permissions
        Path testFile = path.resolve(".test");
        Files.createFile(testFile);
        Files.delete(testFile);
      } catch (FileAlreadyExistsException e) {
        log.warn("Directory {} already exists", path);
      }
    }
  }

  public String storeFile(MultipartFile file, String subDirectory) {
    if (file == null) {
      throw new IllegalArgumentException("File cannot be null");
    }
    if (subDirectory == null || subDirectory.trim().isEmpty()) {
      throw new IllegalArgumentException("Subdirectory cannot be null or empty");
    }

    String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
    String fileName = System.currentTimeMillis() + "_" + originalFileName;

    try {
      if (file.isEmpty()) {
        throw new IllegalArgumentException("Failed to store empty file " + fileName);
      }

      if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
        throw new IllegalArgumentException("Filename contains invalid characters: " + fileName);
      }

      Path targetLocation = Path.of(baseUploadDir, subDirectory).resolve(fileName);

      // Verify directory exists and is writable
      Path directory = targetLocation.getParent();
      if (!Files.exists(directory)) {
        Files.createDirectories(directory);
      }

      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      log.info("Stored file: {} in directory: {}", fileName, subDirectory);
      return fileName;

    } catch (AccessDeniedException e) {
      log.error("Permission denied while storing file: {} in directory: {}", fileName, subDirectory, e);
      throw new RuntimeException("Permission denied while storing file. Please check directory permissions.");
    } catch (IOException e) {
      log.error("Failed to store file: {} in directory: {}", fileName, subDirectory, e);
      throw new RuntimeException("Could not store file " + fileName + ". " + e.getMessage());
    }
  }

  public Resource loadFileAsResource(String fileName, String subDirectory) {
    try {
      Path filePath = Path.of(baseUploadDir, subDirectory).resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists() && resource.isReadable()) {
        return resource;
      } else {
        log.error("File not found or not readable: {} in directory: {}", fileName, subDirectory);
        throw new RuntimeException("File not found or not accessible: " + fileName);
      }
    } catch (MalformedURLException e) {
      log.error("Invalid file path for: {} in directory: {}", fileName, subDirectory, e);
      throw new RuntimeException("Invalid file path: " + fileName);
    }
  }

  public void deleteFile(String fileName, String subDirectory) {
    try {
      Path filePath = Path.of(baseUploadDir, subDirectory).resolve(fileName).normalize();
      if (!Files.deleteIfExists(filePath)) {
        log.warn("File {} in directory {} did not exist during deletion", fileName, subDirectory);
      } else {
        log.info("Deleted file: {} from directory: {}", fileName, subDirectory);
      }
    } catch (AccessDeniedException e) {
      log.error("Permission denied while deleting file: {} from directory: {}", fileName, subDirectory, e);
      throw new RuntimeException("Permission denied while deleting file: " + fileName);
    } catch (NoSuchFileException e) {
      log.warn("File {} in directory {} not found during deletion", fileName, subDirectory);
    } catch (IOException e) {
      log.error("Error deleting file: {} from directory: {}", fileName, subDirectory, e);
      throw new RuntimeException("Could not delete file " + fileName + ". " + e.getMessage());
    }
  }
}