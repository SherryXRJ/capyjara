package com.capy.capyjara.oss.rpc;

import com.capy.capyjara.common.exception.BusinessException;
import com.capy.capyjara.common.response.Result;
import com.capy.capyjara.oss.api.rpc.OssRpcClient;
import com.capy.capyjara.oss.api.rpc.resp.DownloadPreSignedResp;
import com.capy.capyjara.oss.constant.OssConstant;
import com.capy.capyjara.oss.entity.vo.PreSignedVO;
import com.capy.capyjara.oss.entity.vo.StatObjectVO;
import com.capy.capyjara.oss.service.OssService;
import io.minio.http.Method;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class OssRpcClientImpl implements OssRpcClient {

    @Resource
    private OssService ossService;

    @Override
    public Result<DownloadPreSignedResp> getDownloadPreSignedURL(String fileId) {
        PreSignedVO preSignedURL;
        StatObjectVO statObject;
        try {
            preSignedURL = ossService.getPreSignedURL(Method.GET,
                    OssConstant.PRIVATE_BUCKET_NAME, fileId, OssConstant.DOWNLOAD_EXPIRE_TIME, null);

            statObject = ossService.statObject(OssConstant.PRIVATE_BUCKET_NAME, fileId);
        } catch (Exception e) {
            //  todo
            throw new BusinessException(600, "oss error");
        }

        return Result.ok(DownloadPreSignedResp.builder()
                .fileId(preSignedURL.getFileId())
                .preSignedUrl(preSignedURL.getPreSignedUrl())
                .filename(statObject.getFilename())
                .size(statObject.getSize())
                .build());
    }
}
