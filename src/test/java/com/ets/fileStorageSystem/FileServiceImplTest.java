package com.ets.fileStorageSystem;

import com.ets.fileStorageSystem.constraint.Message;
import com.ets.fileStorageSystem.exception.AppException;
import com.ets.fileStorageSystem.model.File;
import com.ets.fileStorageSystem.repository.FileRepository;
import com.ets.fileStorageSystem.service.serviceImpl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class FileServiceImplTest {

    @Mock
    private FileRepository repository;

    @InjectMocks
    private FileServiceImpl fileService;
    @Value("${file.upload-path}") // Assuming this is how you inject the uploadPath
    private String uploadPath;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        if (uploadPath == null) {
            uploadPath = "/path/to/upload/directory"; // Set your desired upload path here
        }
    }

    @Test
    public void get_file_content_test() {
        String fileName = "sample.png";
        File file = new File();
        file.setFileData("Sample file content".getBytes());

        when(repository.findByName(fileName)).thenReturn(Optional.of(file));

        byte[] result = fileService.getFileContent(fileName);

        assertNotNull(result);
        assertEquals("Sample file content", new String(result));

        verify(repository, times(1)).findByName(fileName);
    }

    @Test
    public void delete_file_success_test() {
        String fileName = "sample.png";

        when(repository.deleteByName(fileName)).thenReturn(true);

        String result = fileService.deleteFile(fileName);

        assertTrue(result.contains(Message.FILE_DELETE_SUCCESS));

        verify(repository, times(1)).deleteByName(fileName);
    }

    @Test
    public void delete_file_not_found_test() {
        String fileName = "nonexistent.png";

        when(repository.deleteByName(fileName)).thenReturn(false);

        String result = fileService.deleteFile(fileName);

        assertTrue(result.contains(Message.FILE_DELETE_FAIL_OR_NOT_FOUND));

        verify(repository, times(1)).deleteByName(fileName);
    }

    @Test
    public void delete_file_exception_test() {
        String fileName = "sample.png";

        when(repository.deleteByName(fileName)).thenThrow(new RuntimeException("Delete error"));

        String result = fileService.deleteFile(fileName);

        assertTrue(result.contains(Message.FILE_DELETE_FAIL));

        verify(repository, times(1)).deleteByName(fileName);
    }

    @Test
    public void get_all_files_test() {
        List<File> fileList = new ArrayList<>();
        fileList.add(new File());
        Page<File> page = new PageImpl<>(fileList);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<File> result = fileService.getAllFiles(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(repository, times(1)).findAll(any(Pageable.class));
    }
    @Test
    public void update_file_delete_test() {
        String deleteFileName = "nonexistent.png";
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "newfile.png",
                "text/plain",
                "Updated file content".getBytes()
        );

        when(repository.deleteByName(deleteFileName)).thenReturn(false);

        assertThrows(AppException.class, () -> fileService.updateFile(deleteFileName, multipartFile));

        verify(repository, times(1)).deleteByName(deleteFileName);
        verify(repository, never()).save(any(File.class));
    }
}