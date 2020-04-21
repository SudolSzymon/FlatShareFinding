package com.szymon.ffproject.dao;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.szymon.ffproject.database.entity.FileEntity;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class S3DAO extends DAO<FileEntity> {

    public static final String MAIN_BUCKET = "roomieappstorage";
    private final AmazonS3 s3Client;

    public S3DAO(AmazonS3 s3Client) {this.s3Client = s3Client;}

    public FileEntity get(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean exist(String id) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void delete(String id) {
        s3Client.deleteObject(MAIN_BUCKET, id);
    }

    @Override
    public void executeSave(FileEntity file) {
        ObjectMetadata data = new ObjectMetadata();
        data.setContentType(file.getContentType());
        data.setContentLength(file.getSize());
        data.setCacheControl("604800");
        data.setLastModified(Date.from(file.getModificationTime().atZone(ZoneId.systemDefault()).toInstant()));
        s3Client.putObject(MAIN_BUCKET, file.getEntityID(), file.getFileStream(), data);
    }

    public String getS3Link(String key) {
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(MAIN_BUCKET, key)
            .withExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2L)))
            .withMethod(HttpMethod.GET);
        return s3Client.generatePresignedUrl(req).toString();
    }
}
