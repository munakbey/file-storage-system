package com.ets.fileStorageSystem.service;

import com.ets.fileStorageSystem.model.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public interface FileService {
    String saveFile(MultipartFile file) throws IOException;
    byte[] getFileContent(String fileName) throws FileNotFoundException;
    String deleteFile(String fileName);
    Page<File> getAllFiles(Pageable pageable);
    String updateFile(String deleteFileName, MultipartFile file);
    Optional<File> getFile(String fileName);
}
