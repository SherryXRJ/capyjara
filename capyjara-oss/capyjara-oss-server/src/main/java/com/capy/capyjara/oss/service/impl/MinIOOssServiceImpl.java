package com.capy.capyjara.oss.service.impl;

import com.capy.capyjara.minio.MinIOProperties;
import com.capy.capyjara.oss.entity.vo.PreSignedVO;
import com.capy.capyjara.oss.entity.vo.StatObjectVO;
import com.capy.capyjara.oss.constant.CacheName;
import com.capy.capyjara.oss.constant.OssConstant;
import com.capy.capyjara.oss.service.OssService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;

@Component
@Slf4j
public class MinIOOssServiceImpl implements OssService {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinIOProperties minIOProperties;

    @Cacheable(value = CacheName.GET_PRESIGNED_URL, key = "#method + '_' + #objectName")
    @Override
    public PreSignedVO getPreSignedURL(Method method, String bucket, String objectName, int expiry, String filename) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        GetPresignedObjectUrlArgs presignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .method(method)
                .bucket(bucket)
                .object(objectName)
                .extraQueryParams(Collections.singletonMap(OssConstant.META_DATA_KEY_PREFIX + OssConstant.META_DATA_KEY_FILE_NAME, filename))
                .expiry(expiry)
                .build();

        String absoluteUrl = minioClient.getPresignedObjectUrl(presignedObjectUrlArgs);
        String relativeUrl = absoluteUrl.replace(getMinioAddress(), StringUtils.EMPTY);
        String path = "/" + bucket + "/" + objectName;

        return PreSignedVO.builder().fileId(objectName).preSignedUrl(relativeUrl).method(method).filename(filename).path(path).build();
    }

    @Cacheable(value = CacheName.STAT_OBJECT, key = "#fileId")
    @Override
    public StatObjectVO statObject(String bucket, String fileId) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        StatObjectArgs args = StatObjectArgs.builder()
                .bucket(bucket)
                .object(fileId)
                .build();

        StatObjectResponse statObject = minioClient.statObject(args);
        long size = statObject.size();
        Map<String, String> userMetadata = statObject.userMetadata();
        String filename = userMetadata.get(OssConstant.META_DATA_KEY_FILE_NAME);

        return StatObjectVO.builder()
                .fileId(fileId)
                .size(convertFileSize(size))
                .filename(filename)
                .build();
    }

    private String convertFileSize(Long byteSize){
        double doubleValue = byteSize.doubleValue();
        Assert.isTrue(doubleValue > 0, "文件大小有误!");
        //  通过byte计算文件大小 根据大小返回单位MB或KB
        return  (doubleValue / 1024) > 1000
                ? (byteSize / 1024 / 1024) + "MB" : (byteSize / 1024) + "KB";

    }

    private String getMinioAddress(){
        String protocol = minIOProperties.isSecure() ? "https://" : "http://";
        return protocol + minIOProperties.getEndpoint() + ":" + minIOProperties.getPort();
    }
}

