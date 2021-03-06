package com.opensesame.core.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class LCFanyiJobDTO {
    /**
     * 如果没有加JSONField注解，那么：
     * 1. 这里的字段名必须跟查询语句中的字段名 一模一样（如果取了别名就跟别名一样）
     * 2. 如果是增删改操作，并且你用的是框架自带的方法，那么必须跟数据表的字段名一模一样
     * 3. 如果数据库是user_name 那么这里也必须是user_name，不可以是userName
     * 如果加了JSONField注解，那么只要保证name属性跟数据库字段名一样即可
     */
    @JSONField(name = "original")
    private String original;

}
