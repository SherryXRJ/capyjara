package com.capy.capyjara.starter.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.capy.capyjara.common.entity.BaseField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * 通用MetaObjectHandler
 * <p>
 * 处理填充公共字段
 */
@Slf4j
public class CommonMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug(this.getClass().getName() + " is working, filling insert fields...");

        LocalDateTime insertTime = LocalDateTime.now();
        metaObject.setValue(BaseField.FIELD_CREATE_AT, insertTime);
        metaObject.setValue(BaseField.FIELD_UPDATE_AT, insertTime);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        metaObject.setValue(BaseField.FIELD_CREATE_BY, retrieveUsername(BaseField.FIELD_CREATE_BY, metaObject, authentication));
        metaObject.setValue(BaseField.FIELD_UPDATE_BY, retrieveUsername(BaseField.FIELD_UPDATE_BY, metaObject, authentication));

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug(this.getClass().getName() + " is working, filling update fields...");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        metaObject.setValue(BaseField.FIELD_UPDATE_AT, LocalDateTime.now());
        metaObject.setValue(BaseField.FIELD_UPDATE_BY, retrieveUsername(BaseField.FIELD_UPDATE_BY, metaObject, authentication));
    }

    private Object retrieveUsername(String field, MetaObject metaObject, Authentication authentication){
        Object objectValue = metaObject.getValue(field);
        //  优先使用业务方手动填充的值
        if (Objects.nonNull(objectValue)) {
            return objectValue;
        }

        //  其次使用Security中的认证信息中的值
        return Objects.isNull(authentication) ? StringUtils.EMPTY : authentication.getName();
    }
}
