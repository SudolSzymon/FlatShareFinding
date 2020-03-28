package com.szymon.ffproject.dao;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.io.File;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class S3DAO implements GenericDAO<File, String> {

    public static final String MAIN_BUCKET = "roomieappstorage";
    private final AmazonS3 s3Client;

    public S3DAO(AmazonS3 s3Client) {this.s3Client = s3Client;}

    public File get(String id) {
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
    public void save(File file) {
        s3Client.putObject(MAIN_BUCKET, file.getName(), file);
    }

    public String saveWithUUID(File file) {
        String id = UUID.randomUUID().toString();
        s3Client.putObject(MAIN_BUCKET, id, file);
        return id;
    }

    public String getS3Link(String key) {
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(MAIN_BUCKET, key)
            .withExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2L)))
            .withMethod(HttpMethod.GET);
        return s3Client.generatePresignedUrl(req).toString();
    }
}
