package com.capy.capyjara.admin.api.remote;

import com.capy.capyjara.admin.entity.po.SysTenant;
import com.capy.capyjara.admin.entity.po.SysUser;
import com.capy.capyjara.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "capyjara-admin-server",path = "/admin")
public interface TenantRemoteService {

    Result<SysTenant> getByUserId(String userId);


}
