package com.opensesame.core.dao;

import com.mars.common.annotation.jdbc.MarsDao;
import com.mars.jdbc.core.annotation.MarsGet;
import com.mars.jdbc.core.annotation.MarsSelect;
import com.mars.jdbc.core.helper.templete.JdbcTemplate;

import java.util.List;
import java.util.Map;


@MarsDao("fanyiDAO")//备注:这个和MarsWrite的变量名一致
public abstract class LCFanyiDAO {


    /**
     * @return 查询ALL翻译结果
     */
    @MarsGet(tableName = "fanyi", primaryKey = "original")
    public abstract List<Map> select();

    /**
     * @return 查询ALL翻译结果数量
     */
    public Integer selectCount() {
        String sql = "select original from fanyi";
        try {
            List<Map> resArr = JdbcTemplate.get().selectList(sql);
            if (resArr == null) {
                return 0;
            }
            Integer resCount = resArr.size();
            if (resCount == 0) {
                System.out.println(resCount);
            }
            return resCount;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @return 查询ALL翻译结果数量
     */
    public Integer selectCountById(String original) {
        String sql = "select * from fanyi where original = '" + original + "'";
        try {
            List<Map> resArr = JdbcTemplate.get().selectList(sql);
            if (resArr == null) {
                return 0;
            }
            Integer resCount = resArr.size();
            if (resCount == 0) {
                System.out.println(resCount);
                return 0;
            }
            return resCount;
        } catch (Exception e) {
            System.out.println("sql + " + sql);
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 查询翻译任务ALL数据
     *
     * @return
     */
//    @MarsGet(tableName = "fanyi_job", primaryKey = "original")
    @MarsSelect(sql = "select * from fanyi_job", resultType = Map.class)
    public abstract List<Map> selectFromJob();//JdbcTemplate.get().selectList("select * from fanyi_job j

    /**
     * @return 查询翻译任务数量
     */
    public Integer selectCountFromJob() {
        String sql = "select original from fanyi_job";
        try {
            List<Map> resArr = JdbcTemplate.get().selectList(sql);
            if (resArr == null) {
                return 0;
            }
            Integer resCount = resArr.size();
            if (resCount == 0) {
                System.out.println(resCount);
            }
            return resCount;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //    @MarsUpdate(tableName = "fanyi_job", operType = OperType.INSERT, primaryKey = "original")

    /**
     * 插入翻译任务
     */
    public int insertFromJob(String original) {
        String sql = "replace INTO fanyi_job (original) VALUES('" + original + "');";
        int res = 0;
        try {
            res = JdbcTemplate.get().update(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return res;
    }

}

//
//    CREATE TABLE `fanyi_job` (
//        `original` varchar(255) NOT NULL,
//        `zh` varchar(255) DEFAULT NULL,
//        `cht` varchar(255) DEFAULT NULL,
//        `en` varchar(255) DEFAULT NULL,
//        `fra` varchar(255) DEFAULT NULL,
//        `spa` varchar(255) DEFAULT NULL,
//        PRIMARY KEY (`original`)
//        ) ENGINE=MyISAM DEFAULT CHARSET=utf8;
//    CREATE TABLE `fanyi` (
//        `original` varchar(255) NOT NULL,
//        `zh` varchar(255) DEFAULT NULL,
//        `cht` varchar(255) DEFAULT NULL,
//        `en` varchar(255) DEFAULT NULL,
//        `fra` varchar(255) DEFAULT NULL,
//        `spa` varchar(255) DEFAULT NULL,
//        PRIMARY KEY (`original`)
//        ) ENGINE=MyISAM DEFAULT CHARSET=utf8;