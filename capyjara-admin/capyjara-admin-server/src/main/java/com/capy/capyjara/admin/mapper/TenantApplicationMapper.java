package com.capy.capyjara.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.capy.capyjara.admin.entity.dto.QueryAppAccessDTO;
import com.capy.capyjara.admin.entity.dto.QueryTenantAccessDTO;
import com.capy.capyjara.admin.entity.vo.AppTenantInfoVo;
import com.capy.capyjara.admin.entity.vo.TenantAppDetailInfoVO;
import com.capy.capyjara.admin.entity.vo.TenantAppInfoVO;
import com.capy.capyjara.admin.entity.po.TenantApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TenantApplicationMapper extends BaseMapper<TenantApplication> {
    int insertSelective(TenantApplication record);

    int updateByPrimaryKeySelective(TenantApplication record);

    IPage<TenantAppInfoVO> selectByParams(IPage<TenantAppInfoVO> page, @Param("dto") QueryAppAccessDTO dto);

    List<TenantAppDetailInfoVO> selectListByParams( @Param("dto") QueryAppAccessDTO dto);

    IPage<AppTenantInfoVo> selectTenantByParams(IPage<TenantAppInfoVO> page, @Param("dto") QueryTenantAccessDTO dto);

    List<TenantAppInfoVO> selectList2ByParams(@Param("dto") QueryAppAccessDTO dto);
}