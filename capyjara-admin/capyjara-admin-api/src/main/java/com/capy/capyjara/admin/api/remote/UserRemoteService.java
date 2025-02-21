package com.capy.capyjara.admin.api.remote;

import com.capy.capyjara.admin.entity.po.SysUser;
import com.capy.capyjara.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "capyjara-admin-server",path = "/admin")
public interface UserRemoteService {


    Result<SysUser> getByUsername(String username);


}
