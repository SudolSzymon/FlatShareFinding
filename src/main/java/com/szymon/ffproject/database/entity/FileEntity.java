package com.szymon.ffproject.database.entity;

import com.szymon.ffproject.util.AppContext;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileEntity implements Entity {

    private static final Logger logger = LoggerFactory.getLogger(FileEntity.class);
    private final MultipartFile file;
    private final LocalDateTime creationTime;
    private final UUID id;
    private LocalDateTime modificationTime;

    public FileEntity(MultipartFile source) {
        file = source;
        creationTime = LocalDateTime.now();
        id = UUID.randomUUID();
    }

    @Override
    public String getEntityID() {
        DateTimeFormatter dateTimeFormatter = AppContext.getBeanByClass(DateTimeFormatter.class);
        return id.toString() + "_" + getCreationTime().format(dateTimeFormatter);
    }

    @Override
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    @Override
    public LocalDateTime getModificationTime() {
        return modificationTime;
    }

    @Override
    public void setModificationTime(LocalDateTime modificationTime) {
        this.modificationTime = modificationTime;
    }


    public InputStream getFileStream() {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            logger.error("Cannot obtain input stream from file", e);
            return null;
        }
    }

    public String getContentType() {
        return file.getContentType();
    }

    public long getSize() {
        return file.getSize();
    }
}
