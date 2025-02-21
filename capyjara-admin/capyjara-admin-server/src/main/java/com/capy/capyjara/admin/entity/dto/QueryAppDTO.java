package com.capy.capyjara.admin.entity.dto;

import com.capy.capyjara.common.entity.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "搜索应用DTO")
public class QueryAppDTO extends PageQueryDTO implements Serializable {

    /**
     * 应用名
     */
    @Schema(description="应用名")
    @Size(max = 16,message = "应用名最大长度要小于 16")
    private String name;


}
