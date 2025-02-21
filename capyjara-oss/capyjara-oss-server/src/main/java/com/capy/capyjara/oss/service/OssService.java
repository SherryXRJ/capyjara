package com.capy.capyjara.oss.service;

import com.capy.capyjara.oss.entity.vo.PreSignedVO;
import com.capy.capyjara.oss.entity.vo.StatObjectVO;
import io.minio.errors.*;
import io.minio.http.Method;

import javax.annotation.Nullable;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface OssService {


    /**
     * 获取文件预签名地址
     *
     * Method.GET       =>    下载文件
     * Method.PUT       =>    上传文件
     * Method.DELETE    =>    删除文件
     *
     * @return 预签名信息
     */
    PreSignedVO getPreSignedURL(Method method, String bucket, String objectName, int expiry, @Nullable String filename)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;


    /**
     * 查询文件基础信息
     * @param bucket    桶名称
     * @param fileId    文件id
     * @return
     */
    StatObjectVO statObject(String bucket, String fileId) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;
}
