package com.cn.boot.sample.wechat.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Chen Nan
 */
@NoArgsConstructor
@Data
public class UpdateIndustryDTO {

    /**
     * industry_id1 : 1
     * industry_id2 : 4
     */

    private String industry_id1;
    private String industry_id2;
}
