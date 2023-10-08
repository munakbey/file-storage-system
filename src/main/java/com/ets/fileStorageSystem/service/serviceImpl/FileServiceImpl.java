package com.ets.fileStorageSystem.service.serviceImpl;

import com.ets.fileStorageSystem.constraint.Message;
import com.ets.fileStorageSystem.exception.AppException;
import com.ets.fileStorageSystem.model.File;
import com.ets.fileStorageSystem.repository.FileRepository;
import com.ets.fileStorageSystem.service.FileService;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @NonNull
    private final FileRepository repository;
    @Value("${file.upload-path}")
    private  String uploadPath;

    @SneakyThrows(IOException.class)
    @Override
    public String saveFile(MultipartFile multipartFile)  {
        // Create upload folder if not exists
        if (Objects.nonNull(uploadPath) && !Files.exists(Path.of(uploadPath))) {
            Files.createDirectories(Path.of(uploadPath));
        }
        String targetPath = this.uploadPath + "/" + multipartFile.getOriginalFilename();
        Files.copy(multipartFile.getInputStream(), Path.of(targetPath));

        File file = repository.save(File.builder()
                .name(multipartFile.getOriginalFilename())
                .extension(multipartFile.getContentType())
                .fileData(multipartFile.getBytes())
                .path( Paths.get(uploadPath, multipartFile.getOriginalFilename()).toString())
                .fileSize(multipartFile.getSize())
                .build());

        if (file != null) {
            return Message.FILE_UPLOAD_SUCCESS + multipartFile.getOriginalFilename();
        } else {
            throw new AppException(Message.FILE_UPLOAD_FAIL + multipartFile.getOriginalFilename());
        }
    }
    @Override
    public byte[] getFileContent(String fileName)  {
        Optional<File> optionalFile = getFile(fileName);

        if (optionalFile.isPresent()) {
            File file = optionalFile.get();
            return file.getFileData();
        } else {
            throw new AppException(Message.FILE_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public String deleteFile(String fileName) {
        try {
            Path fullPath = Paths.get(uploadPath, fileName);
            java.io.File file = fullPath.toFile();

            if (file.exists() && file.delete()) {
                repository.deleteByName(fileName);
                return Message.FILE_DELETE_SUCCESS + fileName;
            } else {
                return Message.FILE_DELETE_FAIL_OR_NOT_FOUND + fileName;
            }
        } catch (Exception e) {
            return Message.FILE_DELETE_FAIL + fileName + ", " + e.getMessage();
        }
    }

    @Override
    public Page<File> getAllFiles(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional
    public String updateFile(String deleteFileName, MultipartFile file) {
       deleteFile(deleteFileName);
     return  saveFile(file);
    }

    @Override
    public Optional<File> getFile(String fileName) {
        return repository.findByName(fileName);
    }
}
