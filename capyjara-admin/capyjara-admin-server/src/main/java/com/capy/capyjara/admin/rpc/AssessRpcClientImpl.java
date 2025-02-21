package com.capy.capyjara.admin.rpc;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.capy.capyjara.admin.mapper.TenantApplicationMapper;
import com.capy.capyjara.admin.api.remote.AssessRpcClient;
import com.capy.capyjara.admin.entity.po.TenantApplication;
import com.capy.capyjara.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssessRpcClientImpl implements AssessRpcClient {
    @Override
    public Result deleteAssess(Integer tenantId) {
        return null;
    }
}
