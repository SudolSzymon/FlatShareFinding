package com.szymon.ffproject.database.entity;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.szymon.ffproject.util.AppContext;
import com.szymon.ffproject.util.DateUtil;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class FileEntity implements Entity {

    private final String id;
    private final LocalDateTime creationTime;
    private LocalDateTime modificationTime;
    private final InputStream is;
    private final String contentType;
    private final long size;

    private FileEntity(InputStream is, String contentType, long size, LocalDateTime creationTime,
                       LocalDateTime modificationTime, String id) {
        this.id = id;
        this.creationTime = creationTime;
        this.modificationTime = modificationTime;
        this.is = is;
        this.contentType = contentType;
        this.size = size;
    }

    public FileEntity(MultipartFile source) throws IOException {
        this(source.getInputStream(),
             source.getContentType(),
             source.getSize(),
             LocalDateTime.now(),
             LocalDateTime.now(),
             UUID.randomUUID().toString());
    }

    public FileEntity(S3Object source, DateTimeFormatter dateTimeFormatter) {
        ObjectMetadata meta = source.getObjectMetadata();
        String[] keyData = source.getKey().split("_");
        is = source.getObjectContent();
        size = meta.getContentLength();
        contentType = meta.getContentType();
        creationTime = LocalDateTime.parse(keyData[1], dateTimeFormatter);
        modificationTime = DateUtil.toLocalDateTime(meta.getLastModified());
        id = keyData[0];
    }


    @Override
    public String getEntityID() {
        DateTimeFormatter dateTimeFormatter = AppContext.getBeanByClass(DateTimeFormatter.class);
        return id + "_" + getCreationTime().format(dateTimeFormatter);
    }

    public LocalDateTime getModificationTime() {
        return modificationTime;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    @Override
    public void setModificationTime(LocalDateTime modificationTime) {
        this.modificationTime = modificationTime;
    }

    public InputStream getInputStream() {
        return is;
    }

    public String getContentType() {
        return contentType;
    }

    public long getSize() {
        return size;
    }
}
