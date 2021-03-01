package com.opensesame.core.dao;

import com.mars.common.annotation.jdbc.MarsDao;
import com.mars.jdbc.core.annotation.MarsGet;

import java.util.List;
import java.util.Map;


@MarsDao("fanyiDAO")//备注:这个和MarsWrite的变量名一致
public abstract class LCFanyiDAO {

    /**
     * 查询ALL数据
     *
     * @return
     */
    @MarsGet(tableName = "fanyi_job", primaryKey = "original")
//    @MarsSelect(sql = "select * from fanyi_job", resultType = Map.class)
    public abstract List<Map> selectFromJob();

}
