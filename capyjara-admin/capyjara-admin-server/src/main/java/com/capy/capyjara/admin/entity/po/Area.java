package com.capy.capyjara.admin.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Schema
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_area")
public class Area {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @Schema(description="主键")
    private Integer id;

    /**
     * 父类编码
     */
    @TableField(value = "parent_id")
    @Schema(description="父类编码")
    private Integer parentId;

    /**
     * 区域编码
     */
    @TableField(value = "area_code")
    @Schema(description="区域编码")
    private Integer areaCode;

    /**
     * 级别 1 省 2 市 3 区县 4 街道等
     */
    @TableField(value = "area_level")
    @Schema(description="级别 1 省 2 市 3 区县 4 街道等")
    private Integer areaLevel;

    /**
     * 名称
     */
    @TableField(value = "area_name")
    @Schema(description="名称")
    private String areaName;

    @TableField(value = "lon")
    @Schema(description="")
    private String lon;

    @TableField(value = "lat")
    @Schema(description="")
    private String lat;

    /**
     * 逻辑删除 0 未删除 1 删除
     */
    @TableField(value = "deleted")
    @Schema(description="逻辑删除 0 未删除 1 删除")
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;

    @TableField(value = "create_time")
    @Schema(description="")
    private Date createTime;

    @TableField(value = "update_time")
    @Schema(description="")
    private Date updateTime;

}