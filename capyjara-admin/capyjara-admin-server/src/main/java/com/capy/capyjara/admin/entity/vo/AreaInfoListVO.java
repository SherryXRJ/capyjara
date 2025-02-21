package com.capy.capyjara.admin.entity.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author 
 * 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AreaInfoListVO extends AreaInfoVO {

    private List<AreaInfoVO> childAreas;

}