package com.ets.fileStorageSystem.util.validator;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class FileTypeValidator implements ConstraintValidator<ValidFileType, MultipartFile> {
    private final List<String> allowedExtensions = Arrays.asList("png", "jpeg", "jpg", "docx", "pdf", "xlsx");

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // No file provided, so it's considered valid
        }

        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        return allowedExtensions.contains(fileExtension);
    }
}
