package com.ets.fileStorageSystem.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "file")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;
    private String name;
    private String path;
    @Column(name = "size")
    private Long fileSize;
    private String extension;
    @Lob
    @Column(name="file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData;

    public File() {}
}
