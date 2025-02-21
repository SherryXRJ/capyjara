package com.capy.capyjara.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.capy.capyjara.admin.entity.po.Area;
import com.capy.capyjara.admin.entity.vo.AreaInfoListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AreaMapper extends BaseMapper<Area> {

    List<AreaInfoListVO> getAllAreaList(@Param("query") String query);
}