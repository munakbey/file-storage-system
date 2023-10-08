package com.ets.fileStorageSystem.controller;

import com.ets.fileStorageSystem.constraint.Message;
import com.ets.fileStorageSystem.model.File;
import com.ets.fileStorageSystem.service.FileService;
import com.ets.fileStorageSystem.util.validator.ValidFileType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileRestController {
    private final List<String> allowedExtensions = Arrays.asList("png", "jpeg", "jpg", "docx", "pdf", "xlsx");

    private final FileService fileService;
    @SneakyThrows(IOException.class)
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file")
                                             //@ValidFileType
                                             @Size(max = 5 * 1024 * 1024, message = Message.FILE_SIZE_NOT_APPROPRIATE)
                                             @NotNull MultipartFile file){
        if(!allowedExtensions.contains(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1))){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Message.FILE_NOT_APPROPRIATE);
        }
        String uploadImage = fileService.saveFile(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }
    @SneakyThrows(IOException.class)
    @GetMapping("/download")
    public ResponseEntity<byte[]> getFileContent(@RequestParam("name")  String fileName){
        byte[] fileData = fileService.getFileContent(fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        ByteArrayResource resource = new ByteArrayResource(fileData);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileData.length)
                .body(fileData);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFileContent(@RequestParam("name")  String fileName) {
        try {
            String result = fileService.deleteFile(fileName);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateFile(@RequestParam("deleteFileName")  String deleteFileName, @RequestParam("file")
                                         @Size(max = 5 * 1024 * 1024, message = Message.FILE_SIZE_NOT_APPROPRIATE)
                                         @NotNull MultipartFile file) throws IOException {
        String updateFile = fileService.updateFile(deleteFileName, file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(updateFile);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<File>> getAll(com.ets.fileStorageSystem.model.dao.request.Page page)  {
        Pageable pageableWithoutSort = PageRequest.of(page.page(), page.size());
        Page<File> filesPage = fileService.getAllFiles(pageableWithoutSort);
        return ResponseEntity.status(HttpStatus.OK).body(filesPage);
    }


}
