package com.ets.fileStorageSystem.repository;

import com.ets.fileStorageSystem.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  FileRepository  extends JpaRepository<File,Long> {
    Optional<File> findByName(String fileName);
   Boolean deleteByName(String name);
}
