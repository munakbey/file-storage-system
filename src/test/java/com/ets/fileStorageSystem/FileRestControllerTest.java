package com.ets.fileStorageSystem;
import com.ets.fileStorageSystem.controller.FileRestController;
import com.ets.fileStorageSystem.model.File;
import com.ets.fileStorageSystem.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.NestedServletException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class FileRestControllerTest {
    @Mock
    private FileService fileService;

    @InjectMocks
    private FileRestController fileRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void upload_image_test() throws IOException {
        // Create a sample MultipartFile
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "sample.png",
                MediaType.TEXT_PLAIN_VALUE,
                "Sample file content".getBytes()
        );

        when(fileService.saveFile(any(MultipartFile.class))).thenReturn("sample.png");

        ResponseEntity<?> responseEntity = fileRestController.uploadFile(multipartFile);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("sample.png", responseEntity.getBody());

        verify(fileService, times(1)).saveFile(any(MultipartFile.class));
    }

    @Test
    public void get_file_content_test() throws IOException {
        String fileName = "sample.png";
        byte[] fileData = "Sample file content".getBytes();

        when(fileService.getFileContent(fileName)).thenReturn(fileData);

        ResponseEntity<byte[]> responseEntity = fileRestController.getFileContent(fileName);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(fileData.length, responseEntity.getBody().length);

        verify(fileService, times(1)).getFileContent(fileName);
    }

    @Test
    public void delete_file_content_test() {
        String fileName = "sample.png";

        when(fileService.deleteFile(fileName)).thenReturn("File deleted successfully");

        ResponseEntity<String> responseEntity = fileRestController.deleteFileContent(fileName);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("File deleted successfully", responseEntity.getBody());

        verify(fileService, times(1)).deleteFile(fileName);
    }

    @Test
    public void update_file_test() throws IOException {
        String deleteFileName = "oldfile.png";
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "newfile.png",
                MediaType.TEXT_PLAIN_VALUE,
                "Updated file content".getBytes()
        );

        when(fileService.updateFile(deleteFileName, multipartFile)).thenReturn("newfile.png");

        ResponseEntity<?> responseEntity = fileRestController.updateFile(deleteFileName, multipartFile);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("newfile.png", responseEntity.getBody());

        verify(fileService, times(1)).updateFile(deleteFileName, multipartFile);
    }

    @Test
    public void get_all_test() {
        List<File> fileList = new ArrayList<>();
        fileList.add(File.builder().name("file1.png").build());
        fileList.add(File.builder().name("file2.png").build());
        Page<File> page = new PageImpl<>(fileList);

        Pageable pageable = PageRequest.of(0, 10);

        when(fileService.getAllFiles(pageable)).thenReturn(page);

        // Call the getAll method
        ResponseEntity<Page<File>> response = fileRestController.getAll(new com.ets.fileStorageSystem.model.dao.request.Page(0, 10));

        // Verify the response
        verify(fileService, times(1)).getAllFiles(pageable);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getContent().size() == 2; // Assuming two files in the test data
    }

    @Test
    public void upload_file_with_valid_extension_test() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.pdf");

        ResponseEntity<?> response = fileRestController.uploadFile(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
