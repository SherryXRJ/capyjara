package com.capy.capyjara.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.capy.capyjara.admin.api.contsant.AdminCacheName;
import com.capy.capyjara.admin.entity.po.Area;
import com.capy.capyjara.admin.entity.vo.AreaInfoListVO;
import com.capy.capyjara.admin.entity.vo.AreaInfoVO;
import com.capy.capyjara.admin.mapper.AreaMapper;
import com.capy.capyjara.admin.service.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
//@Service

public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaMapper areaMapper;

    @Override
    @Cacheable(value = AdminCacheName.GET_ALL_AREA_LIST, key = "#query")
    public List<AreaInfoListVO> getAllAreaList(String query) {
        return areaMapper.getAllAreaList( query);
    }

    @Override
    public List<AreaInfoVO> getChildAreaByParentCode(String code) {
        List<Area> areaList = areaMapper.selectList(new QueryWrapper<Area>().lambda().eq(Area::getAreaCode, code));
        return BeanUtil.copyToList(areaList, AreaInfoVO.class);
    }

    @Override
    public List<AreaInfoVO> provinceList() {
        List<Area> areaList = areaMapper.selectList(new QueryWrapper<Area>().lambda().eq(Area::getAreaLevel, 1));
        List<AreaInfoVO> areaInfoVOList=BeanUtil.copyToList(areaList, AreaInfoVO.class);
        return areaInfoVOList;
    }
}
