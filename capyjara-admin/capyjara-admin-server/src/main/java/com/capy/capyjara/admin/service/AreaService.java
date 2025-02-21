package com.capy.capyjara.admin.service;

import com.capy.capyjara.admin.entity.vo.AreaInfoListVO;
import com.capy.capyjara.admin.entity.vo.AreaInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaService {

    List<AreaInfoListVO> getAllAreaList(String query);

    List<AreaInfoVO> getChildAreaByParentCode(@Param("code") String code);

    List<AreaInfoVO> provinceList();

   ;
}
