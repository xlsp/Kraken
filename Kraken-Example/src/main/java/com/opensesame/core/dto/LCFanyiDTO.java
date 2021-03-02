package com.opensesame.core.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class LCFanyiDTO extends LCFanyiJobDTO {
    

    /**
     * String zh 中文
     */
    @JSONField(name = "zh")
    private String zh;
    /**
     * String cht 中文
     */
    @JSONField(name = "cht")
    private String cht;
    /**
     * String en 英语
     */
    @JSONField(name = "en")
    private String en;
    /**
     * String fra 法语
     */
    @JSONField(name = "fra")
    private String fra;
    /**
     * String spa 西班牙
     */
    @JSONField(name = "spa")
    private String spa;
}
