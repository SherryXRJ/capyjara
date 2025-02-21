package com.capy.capyjara.oss.config;

import com.capy.capyjara.oss.constant.OssConstant;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Component
public class MinIOClientInitInitializer implements ApplicationListener<ApplicationStartedEvent> {

    @Resource
    private MinioClient minioClient;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        createBucketIfNotExists(OssConstant.PRIVATE_BUCKET_NAME, OssConstant.PRIVATE_BUCKET_POLICY);
        createBucketIfNotExists(OssConstant.PUBLIC_BUCKET_NAME, OssConstant.PUBLIC_BUCKET_POLICY );
    }

    private void createBucketIfNotExists(String bucketName, String jsonConfig) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(jsonConfig).build());
            }
        } catch (Exception e) {
            log.error("=> 初始化配置MinIO Bucket失败", e);
            throw e;
        }

    }
}
