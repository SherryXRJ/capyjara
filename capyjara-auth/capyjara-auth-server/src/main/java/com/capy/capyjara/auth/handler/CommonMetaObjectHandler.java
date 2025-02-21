package com.capy.capyjara.auth.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.capy.capyjara.auth.api.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;


@Component
@Slf4j
public class CommonMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", new Date());
        metaObject.setValue("updateTime", new Date());
        if (Objects.nonNull(SecurityUtil.getLoginUser())) {
            metaObject.setValue("createUser", SecurityUtil.getLoginUser().getUserId());
            metaObject.setValue("updateUser", SecurityUtil.getLoginUser().getUserId());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", new Date());
        if (Objects.nonNull(SecurityUtil.getLoginUser())) {
            metaObject.setValue("updateUser", SecurityUtil.getLoginUser().getUserId());
        }
    }
}
