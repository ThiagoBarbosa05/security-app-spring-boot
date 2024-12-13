package com.example.security.storage.service;

import com.example.security.exceptions.EmptyFileException;
import com.example.security.exceptions.InvalidFileTypeException;
import com.example.security.storage.provider.StorageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

  @Autowired
  private StorageProvider storageProvider;

  private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
    "image/jpeg",
    "image/png",
    "image/gif",
    "image/webp",
    "image/svg"
  );

  @Override
  public String uploadFile(MultipartFile file) {

    if (file.isEmpty()) {
      throw new EmptyFileException();
    }

    if (!isImageFile(file)) {
      throw new InvalidFileTypeException();
    }

    String key = UUID.randomUUID() + "-" + file.getOriginalFilename();

    storageProvider.upload(file, key);

    return key;
  }

  @Override
  @Async
  public void deleteFile(String key) {
    storageProvider.delete(key);
  }

  private boolean isImageFile(MultipartFile file) {
    String contentType = file.getContentType();
    return contentType != null && ALLOWED_CONTENT_TYPES.contains(contentType);
  }

}
