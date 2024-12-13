package com.example.security.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
  String uploadFile(MultipartFile file);
  void deleteFile(String key);
}
