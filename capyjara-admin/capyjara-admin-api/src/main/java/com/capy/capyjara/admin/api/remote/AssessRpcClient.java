package com.capy.capyjara.admin.api.remote;

import com.capy.capyjara.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

//@FeignClient(value = "capyjara-admin-server",path = "/admin")
public interface AssessRpcClient {

    @PostMapping(value = "/rpc/delete/{tenantId}")
    Result deleteAssess(@PathVariable("tenantId") Integer tenantId);
}
