package com.opensesame.api.vo;

import com.mars.common.annotation.api.MarsDataCheck;
import lombok.Data;

/**
 * 所有实体类都支持lombok，这里为了兼容所有的环境，就还是采用的原始的get，set
 */
@Data
public class LCFanyiVO {

    @MarsDataCheck(notNull = true, msg = "query不可以为空")
    private String query;

    private String lang;
}
