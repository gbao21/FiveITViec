package com.DATN.FiveITViec.model;

import lombok.Data;

@Data
public class Province {
    private Long provinceId;
    private String provinceName;

    public Province(Long provinceId, String provinceName) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
    }
}
