package com.ets.fileStorageSystem.constraint;

public interface Message {
    String FILE_SIZE_NOT_APPROPRIATE = "File size is not appropriate. Maximum allowed file size is 5MB.";
    String FILE_NOT_APPROPRIATE = "File extension is not appropriate.";
    String FILE_NOT_FOUND = "File not found";
    String FILE_UPLOAD_SUCCESS = "File uploaded successfully:";
    String FILE_UPLOAD_FAIL = "Failed to save the file: ";
    String FILE_DELETE_SUCCESS = "File deleted successfully: ";
    String FILE_DELETE_FAIL_OR_NOT_FOUND = "File not found or failed to delete:";
    String FILE_DELETE_FAIL = "Error deleting file:";

}
