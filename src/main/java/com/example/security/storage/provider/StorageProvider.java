package com.example.security.storage.provider;

import org.springframework.web.multipart.MultipartFile;

public interface StorageProvider {
  void upload(MultipartFile file, String key);
  void delete(String key);
}
