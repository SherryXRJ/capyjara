package com.capy.capyjara.common.entity;


import lombok.Data;

import java.util.Objects;

@Data
public class PageQueryDTO {

    private Integer pageNum;


    private Integer pageSize;

    public void defaultPage() {
        if (Objects.isNull(pageNum)) {
            pageNum = 1;
        }
        if (Objects.isNull(pageSize)) {
            pageSize = 10;
        }
    }
}
