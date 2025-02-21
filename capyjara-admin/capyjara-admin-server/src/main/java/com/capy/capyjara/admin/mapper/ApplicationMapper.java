package com.capy.capyjara.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.capy.capyjara.admin.entity.po.Application;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplicationMapper extends BaseMapper<Application> {
    int insertSelective(Application record);

    int updateByPrimaryKeySelective(Application record);
}