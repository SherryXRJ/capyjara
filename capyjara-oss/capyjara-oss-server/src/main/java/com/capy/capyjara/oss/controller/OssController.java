package com.capy.capyjara.oss.controller;

import com.capy.capyjara.common.response.Result;
import com.capy.capyjara.oss.constant.OssConstant;
import com.capy.capyjara.oss.entity.vo.PreSignedVO;
import com.capy.capyjara.oss.service.OssService;
import io.minio.http.Method;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

@Validated
@Tag(name = "Oss-Api", description = "文件操作相关接口")
@RestController
@RequestMapping("/file/v1")
public class OssController {

    @Resource
    private OssService ossService;

    @Operation(summary = "获取文件上传预签名地址")
    @ResponseBody
    @Parameters({
            @Parameter(name = "filename", description = "文件原始名"),
            @Parameter(name = "isPublic", description = "是否是公共文件 isPublic: true 用于上传Logo等可直接下载的公共文件。 isPublic: false 文件上传后，必须获取预签名地址才能下载"),
    })
    @GetMapping("/getUploadPreSignedURL")
    public Result<PreSignedVO> getUploadPreSignedURL(@RequestParam String filename, @RequestParam(defaultValue = "true") boolean isPublic) throws Exception {
        String bucket = isPublic ? OssConstant.PUBLIC_BUCKET_NAME : OssConstant.PRIVATE_BUCKET_NAME;
        return Result.ok(ossService.getPreSignedURL(Method.PUT, bucket, UUID.randomUUID().toString(),
                OssConstant.UPLOAD_EXPIRE_TIME, filename));
    }

}
