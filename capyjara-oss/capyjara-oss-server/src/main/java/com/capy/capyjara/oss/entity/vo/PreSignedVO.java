package com.capy.capyjara.oss.entity.vo;

import io.minio.http.Method;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "文件预签名VO")
public class PreSignedVO {

    @Schema(description = "预签名url地址")
    private String preSignedUrl;

    @Schema(description = "文件id")
    private String fileId;

    @Schema(description = "url的HTTP请求方式 (GET:下载 DELETE:删除 PUT:上传)")
    private Method method;

    @Schema(description = "文件原始名")
    private String filename;

    @Schema(description = "文件存储的路径")
    private String path;

}
