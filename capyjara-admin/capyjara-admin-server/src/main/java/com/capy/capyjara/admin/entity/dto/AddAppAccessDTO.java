package com.capy.capyjara.admin.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema()
public class AddAppAccessDTO implements Serializable {

    @Schema(description = "应用id")
    List<Integer> appIds;

    @Schema(description = "租户id")
    Integer tenantId;
}
