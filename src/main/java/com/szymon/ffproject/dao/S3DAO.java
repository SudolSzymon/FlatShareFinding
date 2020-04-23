package com.szymon.ffproject.dao;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.szymon.ffproject.database.entity.FileEntity;
import com.szymon.ffproject.util.DateUtil;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class S3DAO extends DAO<FileEntity> {

    public static final String MAIN_BUCKET = "roomieappstorage";
    public static final String STATIC_DATA_FOLDER = "staticData/";
    public static final String CACHE_CONTROL = String.valueOf(TimeUnit.DAYS.toSeconds(7L));
    private final DateTimeFormatter dateTimeFormatter;
    private final AmazonS3 s3Client;

    public S3DAO(DateTimeFormatter dateTimeFormatter, AmazonS3 s3Client) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.s3Client = s3Client;
    }

    @Override
    public FileEntity get(String id) {
        S3Object object = s3Client.getObject(MAIN_BUCKET, id);
        return new FileEntity(object, dateTimeFormatter);
    }


    @Override
    public boolean exist(String id) {
        return s3Client.doesObjectExist(MAIN_BUCKET, id);
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
        data.setCacheControl(CACHE_CONTROL);
        data.setLastModified(DateUtil.toDate(file.getModificationTime()));
        s3Client.putObject(MAIN_BUCKET, file.getEntityID(), file.getInputStream(), data);
    }

    public String getS3Link(String key) {
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(MAIN_BUCKET, key)
            .withExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(2L)))
            .withMethod(HttpMethod.GET);
        return s3Client.generatePresignedUrl(req).toString();
    }
}
