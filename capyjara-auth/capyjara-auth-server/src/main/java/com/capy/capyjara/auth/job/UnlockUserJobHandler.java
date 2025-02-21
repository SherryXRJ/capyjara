package com.capy.capyjara.auth.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Slf4j
@Component
public class UnlockUserJobHandler {


    @XxlJob("unlock-user-job-handler")
    public ReturnT<String> unlockUserJob(String param){
        return ReturnT.SUCCESS;
    }
}
