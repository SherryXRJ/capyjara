package com.capy.capyjara.auth.api.rpc;

import com.capy.capyjara.auth.api.rpc.resp.ClientDTO;
import com.capy.capyjara.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "light-weight-server",path = "/auth")
public interface AuthRpcClient {

    /**
     * 新增client
     * @param clientClientDTO dto
     * @return result
     */
    @PostMapping(value = "/rpc/client/add",consumes = "application/json")
    Result addClient(@RequestBody ClientDTO clientClientDTO);

    /**
     * 更新client
     * @param clientUpdateDTO dto
     * @return result
     */
    @PostMapping(value = "/rpc/client/update",consumes = "application/json")
    Result updateClient(@RequestBody ClientDTO clientUpdateDTO);


    /**
     * 修改应用时修改用户授权
     * @param id 应用id
     * @return result
     */
    @PostMapping(value = "/rpc/user/grant/update/{id}",consumes = "application/json")
    Result updateGrant(@PathVariable("id") Integer id);



}
