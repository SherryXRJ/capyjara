package com.capy.capyjara.oss.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "文件基础信息VO")
public class StatObjectVO {

    @Schema(description = "文件id")
    private String fileId;

    @Schema(description = "文件大小")
    private String size;

    @Schema(description = "文件原始名称")
    private String filename;

}
