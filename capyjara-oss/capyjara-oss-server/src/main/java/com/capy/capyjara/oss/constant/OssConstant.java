package com.capy.capyjara.oss.constant;

public interface OssConstant {
    /**
     * MinIO private桶名称
     */
    String PRIVATE_BUCKET_NAME = "capyjara-private";

    /**
     * MinIO public桶名称
     */
    String PUBLIC_BUCKET_NAME = "capyjara-public";

    /**
     * private桶策略
     */
    String PRIVATE_BUCKET_POLICY =
            "{\n" +
            "    \"Version\": \"2012-10-17\",\n" +
            "    \"Statement\": [\n" +
            "        \n" +
            "    ]\n" +
            "}";

    /**
     * public桶策略
     */
    String PUBLIC_BUCKET_POLICY =
            "{\n" +
            "    \"Version\": \"2012-10-17\",\n" +
            "    \"Statement\": [\n" +
            "        {\n" +
            "            \"Effect\": \"Allow\",\n" +
            "            \"Principal\": {\n" +
            "                \"AWS\": [\n" +
            "                    \"*\"\n" +
            "                ]\n" +
            "            },\n" +
            "            \"Action\": [\n" +
            "                \"s3:GetObject\",\n" +
            "                \"s3:ListMultipartUploadParts\",\n" +
            "                \"s3:AbortMultipartUpload\"\n" +
            "            ],\n" +
            "            \"Resource\": [\n" +
            "                \"arn:aws:s3:::capyjara-public/*\"\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    /**
     * 上传文件预签名url有效期(单位: 秒)
     */
    int UPLOAD_EXPIRE_TIME = 60 * 10;

    /**
     * 下载文件预签名url有效期(单位: 秒)
     */
    int DOWNLOAD_EXPIRE_TIME = 60 * 60 * 24;

    /**
     * AMS meta-data请求头前缀
     */
    String META_DATA_KEY_PREFIX = "x-amz-meta-";

    /**
     * 自定义meta-data key: 文件原始名
     */
    String META_DATA_KEY_FILE_NAME = "filename";
}
