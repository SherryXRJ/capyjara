package com.capy.capyjara.common.entity;//package com.capy.capyjara.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@SuperBuilder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity implements Serializable {

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(value = BaseField.FIELD_CREATE_AT, fill = FieldFill.INSERT)
    private LocalDateTime createAt;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    @TableField(value = BaseField.FIELD_CREATE_BY, fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    @TableField(value = BaseField.FIELD_UPDATE_AT, fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt;

    /**
     * 修改人
     */
    @Schema(description = "修改人")
    @TableField(value = BaseField.FIELD_UPDATE_BY, fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 删除标志位
     */
    @Schema(description = "删除标志位")
    @TableLogic(value = "0", delval = "1")
    @TableField(value = BaseField.FILED_DEL_FLAG, fill = FieldFill.INSERT_UPDATE)
    private Boolean delFlag;
}
