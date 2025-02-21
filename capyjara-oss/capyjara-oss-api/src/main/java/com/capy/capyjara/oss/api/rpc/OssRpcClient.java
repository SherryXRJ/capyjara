package com.capy.capyjara.oss.api.rpc;

import com.capy.capyjara.common.response.Result;
import com.capy.capyjara.oss.api.rpc.resp.DownloadPreSignedResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "capyjara-oss-server", path = "/oss")
public interface OssRpcClient {

    /**
     * RPC 获取文件下载预签名地址
     * @param fileId    文件id(ObjectName)
     * @return 下载信息
     */
    @GetMapping("/rpc/getDownloadPreSignedURL")
    Result<DownloadPreSignedResp> getDownloadPreSignedURL(@RequestParam("fileId") String fileId);

}
