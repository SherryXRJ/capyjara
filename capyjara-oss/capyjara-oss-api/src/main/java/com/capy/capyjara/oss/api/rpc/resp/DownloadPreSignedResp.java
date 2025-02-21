package com.capy.capyjara.oss.api.rpc.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DownloadPreSignedResp {

    /**
     * 下载预签名地址
     */
    private String preSignedUrl;

    /**
     * 文件id (minio的ObjectName)
     */
    private String fileId;

    /**
     * 文件大小
     */
    private String size;

    /**
     * 文件名
     */
    private String filename;
}
