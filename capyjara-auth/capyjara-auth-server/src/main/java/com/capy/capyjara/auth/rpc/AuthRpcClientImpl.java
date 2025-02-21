package com.capy.capyjara.auth.rpc;

import com.capy.capyjara.auth.api.rpc.AuthRpcClient;
import com.capy.capyjara.auth.api.rpc.resp.ClientDTO;
import com.capy.capyjara.common.response.Result;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRpcClientImpl implements AuthRpcClient {

    @Override
    public Result addClient(ClientDTO clientClientDTO) {
        return Result.ok();
    }

    @Override
    public Result updateClient(ClientDTO clientUpdateDTO) {
        return Result.ok();
    }

    @Override
    public Result updateGrant(Integer id) {
        return Result.ok();
    }


}
